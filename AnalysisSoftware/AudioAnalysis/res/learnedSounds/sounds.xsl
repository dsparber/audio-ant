<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
  <h2>Recorded Sounds</h2>
  <table border="1">
    <tr bgcolor="#9acd32">
      <th>Number</th>
      <th>Name</th>
      <th>Path</th>
    </tr>
    <xsl:for-each select="recorded_sounds/sound">
    <tr>
      <td><xsl:value-of select="number"/></td>
      <td><xsl:value-of select="name"/></td>
      <td><xsl:value-of select="path"/></td>
    </tr>
    </xsl:for-each>
  </table>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>