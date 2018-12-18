# BytesParser

BytesParser用于实现Bytes序列化，和Bytes反序列化


#### 添加依赖库

将BytesParser加入你的项目中。比如Gradle构建的项目
```
implementation "com.sht:bytesparser:0.x"
```
(x是指最新的项目版本16 )

#### 例子

```
public class Student implements BytesSerializable {
	 @BytesInfo(order = 0)
	 int id;
}
```

```
BytesParser bytesParser = new BytesParser.Builder()
                .order(ByteOrder.BIG_ENDIAN)
                .build();
Student stu1 = new Student;
byte[] datas = bytesParser.toBytes(stu1);
Student stu2 = bytesParser.toBean(datas);
```

更多的使用参考见： <a href="src/test/java/com/sht/bytesparser/annonation/" target="_blank">Annotion Test</a>

#### 重要的类和接口

##### BytesSerializable 接口

* 只有实现了BytesSerializable 接口的类，BytesParser才能序列化和反序列化该类
##### BytesInfo 注解
* 类中的Field注解了BytesBytesInfo，才会序列化和反序列该Field
* 详细讲解
	- order 表示该Field在序列化和反序列的顺序，从0开始，必须连续
	- len 表示基础类型的所占有的Bytes长度，或者表示集合或者数组的个数
	- sign 用于表示整数类型（short，int，long）是否有符号
	- charsetName 用于标定字符串转成bytes数组的编码
	- lenFlagBytesSize 用于处理不定长的String，数组，集合的长度单位所占用的bytes长度
	
#### 支持类型

  + 原始类型: boolean、byte、char、short、int、float、long、double
  + 原始类型的封装类
  + Field是实现BytesSerializable 的类的对象
  + Reserved 对象，用于保留的bytes的长度，即调过解析的
  + Array
  + Collection

##### 原始类型Bytes的默认长度

|参考| boolean| byte| char| shot| int| long|float| double|
|-------|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| default length| 1| 1| 2 |2|4|8|4|8|

##### 注意事项


+ 实现BytesSerializable的类，必须要有默认的构造函数
+ 当Field是基础类型时，len的长度必须小于等于其默认长度。len为0，就是默认长度。当sign为true是，len < 默认长度
+ 当使用**不定长特性**时，len必须为0，lenFlagBytesSize 必须大于0

##### 复杂的例子

```
public static class Books implements BytesSerializable{
        @BytesInfo(order = 0)
        private int auther;
        @BytesInfo(order = 1, len = 8)
        private double price;
        @BytesInfo(order = 2, lenFlagBytesSize = 1)
        private String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Books books = (Books) o;
            return auther == books.auther &&
                    Double.compare(books.price, price) == 0 &&
                    Objects.equals(name, books.name);
        }

        @Override
        public String toString() {
            return "Books{" +
                    "auther=" + auther +
                    ", price=" + price +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class Writer implements BytesSerializable{
        @BytesInfo(order = 0)
        private int id;
        @BytesInfo(order = 1, len = 20)
        private String name;
        @BytesInfo(order = 2, lenFlagBytesSize = 1)
        private List<Books> books ;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Writer writer = (Writer) o;
            return id == writer.id &&
                    Objects.equals(name, writer.name) &&
                    Objects.equals(books, writer.books);
        }

        @Override
        public String toString() {
            return "Writer{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", books=" + books +
                    '}';
        }
    }

    @Test
    public void testBytesParser(){
        BytesParser bytesParser = new BytesParser.Builder()
                .order(ByteOrder.BIG_ENDIAN)
                .charsetName(Charset.forName("GB2312"))
                .build();
        Writer writer = new Writer();
        writer.name = "孙悟空";
        writer.id = 123456789;
        writer.books = new ArrayList<>();
        String[] names = new String[]{"西游记", "红楼梦", "三国演义", "水浒传"};
        double[] prices = new double[]{12.12, 11.11, 30.58, 98.54};
        for (int i = 0; i < names.length; i++){
            Books book = new Books();
            book.auther = writer.id;
            book.name = names[i];
            book.price = prices[i];
            writer.books.add(book);
        }

        float val = 12.12f;
        long v = Double.doubleToLongBits(val);
        int a = Float.floatToIntBits(val);
        System.out.println(v +" " + a);

        byte[] data = bytesParser.toBytes(writer);
        Writer writer1 = bytesParser.toBean(Writer.class, data);
        Assert.assertEquals(writer, writer1);
        System.out.println(writer1.toString());
        
    }
```
