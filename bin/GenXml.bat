@echo on

SET PATH=C:/Program Files/Java/jdk1.6.0_32/bin
set LOCALCLASSPATH=.;./properties_FE

for %%i in ("..\lib\*.jar") do CALL .\lcp.bat %%i
for %%i in ("..\lib\*.zip") do CALL .\lcp.bat %%i

java -mx356m -ms64m -cp %LOCALCLASSPATH% com.eon.fatr.batch.Batch_FACDIS_FE_GenXml properties_FE.genXml ./properties_FE/log4j_genXml.properties ./SqlMapConfig_FE.xml

