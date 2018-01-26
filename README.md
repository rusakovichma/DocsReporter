# Overview

Documents generation system in docx, odt, pdf, xhtml and pptx formats based on templates.<br/>
Supported template formats: **.docx**, **.odt** and **.pptx**<br/>

## Using

 * Add **repository** in pom.xml:
    ```xml
    <repository>
        <id>DocsReporter-mvn-repo</id>
        <url>https://raw.github.com/creepid/DocsReporter/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
    ```
    
 *  Add **dependency**:
    ```xml
    <dependency>
		<groupId>by.creepid</groupId>
		<artifactId>docsreporter</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	```
 * Then add in context of a Spring application:
	```xml
	<import resource="classpath*:META-INF/spring/docsreporter-context.xml" />
	```
 * Define **reportTemplate** instance:
	```xml
	<bean id="reportTemplate" parent="reportTemplateBase">
        <property name="templatePath">
            <value>/pathto/template/DocxProjectWithVelocity.docx</value>
        </property>
        <property name="modelClass">
            <value>by.creepid.docsreporter.model.Project</value>
        </property> 
        <property name="modelName" value="project"/>
    </bean>
	```
	**templatePath** - path a template (only .docx, .odt and .pptx is supported)<br/>
	**modelClass** - model class (deep hierarchy is possible)<br/>
	**modelName** - context of the model in the template<br/>

 * In source code:
```java
@Autowire
private ReportTemplate reportTemplate;
```
Fill the model and generate the document:

```java
Project project = new Project();
...//fill the object

//If we have any dynamic images in our template define ImageExtractObserver instance:
ImageExtractObserver observer = new ImageExtractObserver() {
    public void getImage(byte[] content, String path) {
    //process image extraction event, maybe saving the image in some folder
    }
};

//choosing output document format (.docx, .odt, .pptx, .pdf, .xhtml available)
DocFormat outFormat = DocFormat.PDF;

//receive processed document and saving it to file, response wrapper, etc.
OutputStream out = reportTemplate.generateReport(outFormat, project, observer);

```
   
