# k-file-detector

#### Description

a tool for detect file type

#### Software Architecture
##### detect file type by magic number or content  
1. reference  https://www.garykessler.net/library/file_sigs.html
2. reference xml/ooxml

#### Installation
#####1、build and publish to local

```java
        gradle clean build publishToMavenLocal -x test
```
#####2、gradle (gradle.org)
```java
        implementation 'io.github.kylin-hunter:k-file-detector:1.0.5'
```
#####3、maven (maven.apache.org)
```java
        <dependency>
          <groupId>io.github.kylin-hunter</groupId>
            <artifactId>k-file-detector</artifactId>
          <version>1.0.5</version>
        </dependency>
```

#### Instructions
1. by file
```java

        DetectResult detectResult = FileDetector.detect(new File("xxxx.xxx"));  //by file
        List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
        for (FileType fileType : possibleFileTypes) {
            System.out.println("id=" + fileType.getId()); // file type id
            System.out.println("extensions=" + fileType.getExtensions()); // the extensions, may be empty
            System.out.println("desc=" + fileType.getDesc()); // the description 
        }
```

2. by InputStream
```java

        try (InputStream in = new FileInputStream(new File("xxx.xxx"))) {
            // if  know the file name， you can invoke:  FileDetector.detect(in,"xxx.xxx)
            DetectResult detectResult = FileDetector.detect(in); 
            List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
            for (FileType fileType : possibleFileTypes) {
                System.out.println("id=" + fileType.getId()); // file type id，
                System.out.println("extensions=" + fileType.getExtensions()); // the extensions, may be empty
                System.out.println("desc=" + fileType.getDesc()); // the description 
            }
        }
```

3. by bytes
```java
        byte[] content = FileUtils.readFileToByteArray(new File("xxx.xxx"));
        // if  know the file name， you can invoke:  FileDetector.detect(content,"xxx.xxx)
        DetectResult detectResult = FileDetector.detect(content); 
        List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
        for (FileType fileType : possibleFileTypes) {
            System.out.println("id=" + fileType.getId()); // file type id
            System.out.println("extensions=" + fileType.getExtensions()); // the extensions, may be empty
            System.out.println("desc=" + fileType.getDesc()); // the description 
        }
```
#### License

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
