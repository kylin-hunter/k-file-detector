# k-file-detector

#### 介绍

一个检查文件类型的工具。

#### 软件架构

#####根据文件的魔数 以及 内容判定文件类型  
1. 参考 https://www.garykessler.net/library/file_sigs.html
2. 参考 xml/ooxml 


#### 安装教程
#####1、编译并发布到本地

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


#### 使用说明
1. 通过file检测
```java

        DetectResult detectResult = FileDetector.detect(new File("xxxx.xxx"));  //通过file检测
        List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
        for (FileType fileType : possibleFileTypes) {
            System.out.println("id=" + fileType.getId()); // 文件类型唯一编号
            System.out.println("extensions=" + fileType.getExtensions()); // 文件类型对应的扩展名，可能是空
            System.out.println("desc=" + fileType.getDesc()); //文件类型描述
        }
```

2. 通过文件流检测
```java

        try (InputStream in = new FileInputStream(new File("xxx.xxx"))) {
            DetectResult detectResult = FileDetector.detect(in); // 如果知道文件名也可以调用  FileDetector.detect(in,"xxx.xxx)
            List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
            for (FileType fileType : possibleFileTypes) {
                System.out.println("id=" + fileType.getId()); // 文件类型唯一编号
                System.out.println("extensions=" + fileType.getExtensions()); // 文件类型对应的扩展名，可能是空
                System.out.println("desc=" + fileType.getDesc()); //文件类型描述
            }
        }
```

3. 通过字节检测
```java
        byte[] content = FileUtils.readFileToByteArray(new File("xxx.xxx"));
        DetectResult detectResult = FileDetector.detect(content); // 如果知道文件名也可以调用  FileDetector.detect(content,"xxx.xxx)
        List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
        for (FileType fileType : possibleFileTypes) {
            System.out.println("id=" + fileType.getId()); // 文件类型唯一编号
            System.out.println("extensions=" + fileType.getExtensions()); // 文件类型对应的扩展名，可能是空
            System.out.println("desc=" + fileType.getDesc()); //文件类型描述
        }
```
#### 版权 | License

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
