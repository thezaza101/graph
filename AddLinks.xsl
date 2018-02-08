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


    <!--xsl:template match="svg:svg">
        <xsl:copy>
                <xsl:attribute name="id">graph00</xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template-->

    <xsl:template match="svg:g[@class = 'node']">

        <xsl:element namespace="http://www.w3.org/2000/svg" name="a">
            <xsl:attribute name="xlink:href"><xsl:value-of select="svg:title/text()"/></xsl:attribute>
            <xsl:copy>
                <xsl:attribute name="style">cursor:hand</xsl:attribute>
                <xsl:apply-templates/>
            </xsl:copy>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>