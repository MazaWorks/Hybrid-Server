<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:l="http://www.esei.uvigo.es/dai/hybridserver">
<xsl:output method="html" indent="yes" encoding="utf-8"/>
<xsl:template match="/">
	<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE HTML&gt;</xsl:text>
	<html>
		<head>
			<title>Configuration</title>
		</head>
		<body>
			<h1>Connections:</h1>
			<xsl:apply-templates select="l:configuration/l:connections"/>
			<h1>Database:</h1>
			<xsl:apply-templates select="l:configuration/l:database"/>
			<h1>Servers:</h1>
			<xsl:apply-templates select="l:configuration/l:servers"/>
		</body>
	</html>
</xsl:template>

<xsl:template match="l:connections">
	<div>
		<ul>
			<li>http: <xsl:value-of select="l:http"/></li>
			<li>webservice: <xsl:value-of select="l:webservice"/></li>
			<li>numClient: <xsl:value-of select="l:numClients"/></li>
		</ul>
	</div>
</xsl:template>

<xsl:template match="l:database">
	<div>
		<ul>
			<li>user: <xsl:value-of select="l:user"/></li>
			<li>password: <xsl:value-of select="l:password"/></li>
			<li>url: <xsl:value-of select="l:url"/></li>
		</ul>
	</div>
</xsl:template>

<xsl:template match="l:servers">	
	<div>
		<xsl:for-each select="l:server">
			<h2><xsl:value-of select="@name"/></h2>
			<ul>
				<li>wsdl: <xsl:value-of select="@wsdl"/></li>
				<li>namespace: <xsl:value-of select="@namespace"/></li>
				<li>service: <xsl:value-of select="@service"/></li>
				<li>httpAddress: <xsl:value-of select="@httpAddress"/></li>
			</ul>
		</xsl:for-each>	
	</div>
</xsl:template>

</xsl:stylesheet>
