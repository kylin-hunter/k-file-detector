# k-file-detector

#### Description

a tool for detect file type

#### Software Architecture
##### detect file type by magic number or content  
1. reference  https://www.garykessler.net/library/file_sigs.html
2. reference xml/ooxml

#### Installation
#####1、gradle (gradle.org)
```java
implementation 'com.kylinhunter.plat:k-file-detector:x.x.x'
```
#####2、maven (maven.apache.org)
```java
        <dependency>
          <groupId>com.kylinhunter.plat</groupId>
            <artifactId>k-file-detector</artifactId>
          <version>x.x.x</version>
        </dependency>
```

#### Instructions
1. by file
```java

        DetectResult detectResult = FileDetector.detect(new File("xxxx.pdf"));  //by file
        List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
        for (FileType fileType : possibleFileTypes) {
            System.out.println(fileType.getId()); // file type id
            System.out.println(fileType.getExtensions()); // the extensions, may be empty
            System.out.println(fileType.getDesc()); // the description 
        }
```

2. by InputStream
```java

        try (InputStream in = new FileInputStream(new File("xxx.pdf"))) {
            // if  know the file name， you can invoke:  FileDetector.detect(in,"xxx.pdf)
            DetectResult detectResult = FileDetector.detect(in); 
            List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
            for (FileType fileType : possibleFileTypes) {
                System.out.println(fileType.getId()); // file type id，
                System.out.println(fileType.getExtensions()); // the extensions, may be empty
                System.out.println(fileType.getDesc()); // the description 
            }
        }
```

3. by bytes
```java
        byte[] content = FileUtils.readFileToByteArray(new File("xxx.pdf"));
        // if  know the file name， you can invoke:  FileDetector.detect(content,"xxx.pdf)
        DetectResult detectResult = FileDetector.detect(content); 
        List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
        for (FileType fileType : possibleFileTypes) {
            System.out.println(fileType.getId()); // file type id
            System.out.println(fileType.getExtensions()); // the extensions, may be empty
            System.out.println(fileType.getDesc()); // the description 
        }
```



