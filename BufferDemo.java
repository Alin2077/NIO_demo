import java.nio.ByteBuffer;

/**
 * Buffer对于除布尔外的基础类型都有对应的
 * ByteBuffer,ShortBuffer,CharBuffer,IntBuffer,LongBuffer,FloatBuffer,DoubleBuffer
 * 最常用的就是ByteBuffer
 * 1.HeapByteBuffer  堆内字节缓存
 * 2.DirectByteBuffer 直接字节缓存，即堆外字节缓存
 * 顾名思义，可知DirectByteBuffer的优点：1.不受堆内存大小限制 2.可直接与电脑操作系统交互 
 * 所以DirectByteBuffer适用的场景有：
 *              1.java程序和本地磁盘或者socket传输数据
 *              2.大文件对象
 *              3.生命周期持续时间长，不需要频繁创建销毁
 */
public class BufferDemo {

    public static void main(String[] args) {
        
        BufferDemo bd = new BufferDemo();
        bd.demo_one();

    }

    private void demo_one(){

        String msg = "java Nio ,hello!";

        ByteBuffer bb = ByteBuffer.allocate(1024);
        
        byte[] bytes = msg.getBytes();

        bb.put(bytes);

        bb.flip();

        byte[] tempArr = new byte[bytes.length];
        int i =0;

        while (bb.hasRemaining()){
            byte b = bb.get();
            tempArr[i++] = b;
        }

        System.out.println(new String(tempArr));
    }
    
}