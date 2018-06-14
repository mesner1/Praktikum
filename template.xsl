<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
<xsl:template match="recept">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block font-size="16pt" font-weight="bold" space-after="5mm">Recept
          </fo:block>
          <fo:block font-size="8pt" font-weight="bold" space-after="5mm">Pregled izdanih zdravil.
          </fo:block>
          <fo:block font-size="10pt">
          <fo:table table-layout="fixed" width="100%" border-collapse="separate">    
            <fo:table-column column-width="3cm"/>
            <fo:table-column column-width="3cm"/>
            <fo:table-column column-width="3cm"/>
             <fo:table-column column-width="3cm"/>
             <fo:table-column column-width="3cm"/>
            <fo:table-body>
              <xsl:apply-templates select="podrobnosti"/>
            </fo:table-body>
          </fo:table>
          </fo:block>
          
           <fo:block font-size="8pt" font-weight="bold" space-after="5mm"> Lekarna
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
     </fo:root>
</xsl:template>
 

          
        
<xsl:template match="podrobnosti">

<fo:table-row>   
     
      <fo:table-cell>
        <fo:block>
         ID
        </fo:block>
      </fo:table-cell>
     
      <fo:table-cell>
        <fo:block>
         CAS
        </fo:block>
      </fo:table-cell>   
      <fo:table-cell>
        <fo:block>
         TIP
        </fo:block>
      </fo:table-cell>
      <fo:table-cell>
        <fo:block>
         AVTOR
        </fo:block>
      </fo:table-cell>
      <fo:table-cell>
        <fo:block>
       DOPOLNILA
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    <fo:table-row>   
     
      <fo:table-cell>
        <fo:block>
          <xsl:value-of select="id"/>
        </fo:block>
      </fo:table-cell>
     
      <fo:table-cell>
        <fo:block>
          <xsl:value-of select="cas"/>
        </fo:block>
      </fo:table-cell>   
      <fo:table-cell>
        <fo:block>
      <xsl:value-of select="tip"/>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell>
        <fo:block>
      <xsl:value-of select="avtor"/>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell>
        <fo:block>
      <xsl:value-of select="dopolnila"/>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    
  </xsl:template>
    
   
           
</xsl:stylesheet>