# Overview

Documents generation system in docx, odt, pdf, xhtml and pptx formats based on templates.<br/>
Supported template formats: **.docx**, **.odt** and **.pptx**<br/>

## Usage

1. Add **repository** in pom.xml:
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
    
2.  Add **dependency**:
    ```xml
    <dependency>
		<groupId>by.creepid</groupId>
		<artifactId>docsreporter</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	```
3. Then add in context of a Spring application:
	```xml
	<import resource="classpath*:META-INF/spring/docsreporter-context.xml" />
	```
4. Create report template and map object fields to the model via [Mergefields](https://kb.blackbaud.com/knowledgebase/Article/97378):
![DocxProjectWithVelocity.docx document template](https://raw.githubusercontent.com/rusakovichma/DocsReporter/59f0225dbad2444d94cd1261cd1a0a9e12399b2f/document-template.png)
5. Define **reportTemplate** bean instance in Spring context:
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

6. In source code generate the document based on the given template:
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

//receive the resulting document content and save it to file, response wrapper, etc.
OutputStream out = reportTemplate.generateReport(outFormat, project, observer);

```
