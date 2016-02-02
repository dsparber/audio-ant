<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />

	<xsl:template match="/">
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="sounds.css" />
			</head>
			<body>
				<h2>Recorded Sounds</h2>
				<form method="post">
					<table>
						<tr>
							<th class="center">ID</th>
							<th class="left">Name</th>
							<th class="left">Path</th>
							<th class="left sound">Sound</th>
						</tr>
						<xsl:for-each select="recorded_sounds/sound">
							<tr>
								<td class="center">
									<xsl:value-of select="number" />
								</td>
								<td class="left">
									<xsl:variable name="name" select="name" />
									<xsl:variable name="number" select="number" />
									<input name="{$number}" type="text" value="{$name}" placeholder="Unnamed sound" />
								</td>
								<td class="left">
									<xsl:value-of select="path" />
								</td>
								<td class="left sound">
									<xsl:variable name="folder" select="path" />
									<audio controls="controls">
										<source src="../../{$folder}learnedSound.wav" type="audio/wav" />
									</audio>
								</td>
							</tr>
						</xsl:for-each>
					</table>
					
					<input type="submit" name="submit" value="Save changes"/>
					
				</form>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>