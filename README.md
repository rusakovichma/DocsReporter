# Overview

Documents reporting sample in docx, odt, pdf, xhtml and pptx formats extending [XDocReport project](https://github.com/opensagres/xdocreport) and using [Spring Framework](http://spring.io/).<br/>
Supported template formats: **.docx**, **.odt** and **.pptx**<br/>

More about XDocReport: https://code.google.com/p/xdocreport/wiki/Overview

# Using

 * For the first, we need any template in supported format to processing ([see example](https://github.com/creepid/DocsReporter/blob/master/src/test/resources/DocxProjectWithVelocity.docx)), corresponding Java model ([see example](https://github.com/creepid/DocsReporter/tree/master/src/test/java/by/creepid/docsreporter/model)) and choose template engine ([Velocity](http://freemarker.org/) or [Freemarker](http://velocity.apache.org/))

Template and Java model creating tutorial:
* [Docx template tutorial](https://code.google.com/p/xdocreport/wiki/DocxReportingJavaMain)
* [Odt template tutorial](https://code.google.com/p/xdocreport/wiki/ODTReportingJavaMain)
* [Pptx template tutorial](https://code.google.com/p/xdocreport/wiki/PPTXReporting)

 * Then add in your pom.xml **repository**:
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
    
    and **dependency**:
    ```xml
    <dependency>
			<groupId>by.creepid</groupId>
			<artifactId>docsreporter</artifactId>
			<version>0.0.1-SNAPSHOT</version>
	</dependency>
	```
 * Then add in **app-context.xml** of your Spring application:
```xml
<import resource="classpath*:META-INF/spring/docsreporter-context.xml" />
```
and define our **reportTemplate** bean:
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
So, as you guess,<br/> 
**templatePath** is path to our template (remember only .docx, .odt, .pptx supported)<br/>
**modelClass** - defines our trunk model class (maybe with deep hierarchy)<br/>
**modelName** - name of root class in template<br/>

Then in code autowire reportTemplate in any bean of your application (**ReportTemplate** is thread safe):
```java
@Autowire
private ReportTemplate reportTemplate;
```
Create and process project instance:
```java
Project project = new Project();
...//fill the object

//If we have any dynamic images in our template define ImageExtractObserver instance:
ImageExtractObserver observer = new ImageExtractObserver() {
    public void getImage(byte[] content, String path) {
    //process image extraction event, maybe saving it in some folder
    }
};

//choose output document format (.docx, .odt, .pptx, .pdf, .xhtml available)
DocFormat outFormat = DocFormat.PDF;

//receive processed document and saving it to file or to response wrapper etc.
OutputStream out = reportTemplate.generateReport(outFormat, project, observer);

```
That's it!

  
# Configuring Java model
 
   If we want to tell that given class field is image (essentially in byte array)
   and it links to temp image in template with called "logo" bookmark, just to add annotation:
   **@Image(bookmarks = {"logo"})** or **@Image(bookmarks = {"logo1", "logo2"})** if we have several bookmarks([more about template dynamic image](https://code.google.com/p/xdocreport/wiki/DocxReportingJavaMainDynamicImage))

***
    Adding **@Image** annotation:
   ![@Image annotation adding](http://i60.tinypic.com/6y33t1.png)
***
    Given template with marked temp image:
   ![Template with image](http://i61.tinypic.com/10mkllk.png)
***
    And after have:
   ![@Image annotation adding result](http://i61.tinypic.com/2nrmwch.png)  
  
   Other opportunities of **@Image** annotation:
*     **useTemplateSize()** default true - set size of template image in your document
*     **useRatioSize()** default true - define is need to save aspect ratio of image
*     **width()** - forced set image width 
*     **height()** - forced set image height

***

**Note:** DocsReporter allows use even images in CMYK color style and improves images quality after resizing by using special filters

If we have null value of some field in model class to map we can configure the value that will replace this value in **emptyField** property of **contextProcessor** bean:

```xml
<bean id="contextProcessor" class="by.creepid.docsreporter.context.DocContextProcessor"
          scope="prototype" lazy-init="true" depends-on="imageConverter">
        <property name="emptyField">
            <value>_________</value>
        </property>
</bean>
```
***
        The other way: add @NullValue annotation to this field:
   ![Adding @NullValue](http://i58.tinypic.com/j7vz20.png)

So, we have: if mail field of developer in list will be null, we'll see "Enter your mail, please" instead and "_________" in other cases.
![@NullValue result](http://i60.tinypic.com/34j9njn.png)

Now we want to apply text styling in name field of developer in HTML way, [see more about TextStyling](https://code.google.com/p/xdocreport/wiki/DocxReportingJavaMainTextStyling), and then set `developer.setName("<b>Victor</b>")`

![TextStyling example](http://i62.tinypic.com/2rp4efs.png)


***
    We'll see name in bold type in doc:
![Bold text type](http://i60.tinypic.com/21o0xg9.png)

#Add field formatters

We can add our own field formatter by implementing the interface:
```java
public interface DocTypesFormatter<F,T> {

	public T format(F f);

	public Class<T> getToClass();

	public Class<F> getFromClass();

}
```
And define this class as `@Component`.
So, when the context meets the field with given type, it's searching for corresponding formatter, modifies the value and then maps it in target document
   
