import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Channel 管道
 * FileChannel 针对本地文件的管道
 * SocketChannel 通过TCP读取网络数据的管道
 * ServerSocketChannel 监听新进来的TCP连接，如同web服务器，对每一个新进来的连接创建一个SocketChannel
 * DatagramChannel 通过UDP读取网络数据的管道
 * Channel本身不存储数据，而是运送数据。所以Channel需要搭配Buffer来使用
 */
public class ChannelDemo {
    
    public static void main(String[] args) {
        
        ChannelDemo cd = new ChannelDemo();
        // cd.demo_one();
        // cd.demo_two();
        cd.demo_three();

    }

    private void demo_one(){
        File file = new File("tempData/1.txt"); 
        try (FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(new File("tempData/2.txt"));
            FileChannel inputChannel = fileInputStream.getChannel();
            FileChannel outputChannel = fileOutputStream.getChannel();
            ){

            ByteBuffer allocate = ByteBuffer.allocate((int) file.length());
            inputChannel.read(allocate);
            allocate.flip();
            outputChannel.write(allocate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //未完待续
    private void demo_two(){
        File fileOne = new File("tempData/3.txt");
        File fileTwo = new File("tempData/4.txt");
        try (FileInputStream file1InputStream = new FileInputStream(fileOne);
            FileOutputStream file1OutputStream = new FileOutputStream(fileOne);
            FileInputStream file2InputStream = new FileInputStream(fileTwo);
            FileOutputStream file2OutputStream = new FileOutputStream(fileTwo);

            FileChannel input1Channel = file1InputStream.getChannel();
            FileChannel output1Channel = file1OutputStream.getChannel();
            FileChannel input2Channel = file2InputStream.getChannel();
            FileChannel output2Channel = file2OutputStream.getChannel();
            ){

            
            

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void demo_three(){
        try {
            //获取ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
            //绑定地址，端口号
            serverSocketChannel.bind(address);
            //创建一个缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (true) {
                //获取SocketChannel
                SocketChannel socketChannel = serverSocketChannel.accept();
                while (socketChannel.read(byteBuffer) != -1){
                    //打印结果
                    System.out.println(new String(byteBuffer.array()));
                    //清空缓冲区
                    byteBuffer.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
