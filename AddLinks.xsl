<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:svg="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" exclude-result-prefixes="svg">

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <!-- Identity template : copy all text nodes, elements and attributes -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>


    <xsl:template match="svg:g[@class = 'node']">

        <xsl:element name="svg:a">
            <xsl:attribute name="xlink:href"><xsl:value-of select="svg:title/text()"/></xsl:attribute>
            <g class="node" id="{@id}" style="cursor:hand">
                <xsl:apply-templates/>
            </g>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>