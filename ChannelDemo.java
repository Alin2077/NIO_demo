import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
        cd.demo_two();

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



}
