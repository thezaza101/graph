#!/usr/bin/env bash

# Exit immediately if there is an error
set -e

# Cause a pipeline (for example, curl -s http://sipb.mit.edu/ | grep foo) to produce a failure return code if any command errors not just the last command of the pipeline.
set -o pipefail

# Print shell input lines as they are read.
set -v

# Include build env vars
source "$(dirname "$0")/buildrc"

# login to cloud foundry if env vars are present
login() {

  if [[ -z "$CF_ORG" ]]; then
    echo "CF env vars not found, assuming you are already logged in to cf"
    return
  fi

  cf api $CF_API
  cf auth "$CF_USER" "$CF_PASSWORD"
  cf target -o $CF_ORG
  cf target -s $CF_SPACE
}

# main script function
#
main() {
  login
  cf push api-gov-au-graph -f manifest-stating.yml
}

main $@
