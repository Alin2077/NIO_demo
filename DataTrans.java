//Data transmission数据传输

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 */
public class DataTrans {
    
    public static void main(String[] args) {
        DataTrans dts = new DataTrans();
        // dts.channelsTrans();
        // dts.buffTrans();
        dts.buffsTrans();
    }

    private void channelsTrans(){

        long start = System.nanoTime();

        try(FileInputStream fileInputStream = new FileInputStream("tempData/oraignation.m4a");
            FileOutputStream fileOutputStream = new FileOutputStream("tempData/target.m4a");
            FileChannel inChannel = fileInputStream.getChannel();
            FileChannel outChannel = fileOutputStream.getChannel();){

            ByteBuffer allocate = ByteBuffer.allocate(1024);
            while (inChannel.read(allocate) != -1) {
                allocate.flip();  //缓存区写模式，从头开始写
                //allocate.compact(); //切换为写模式,将原来的数据保留
                outChannel.write(allocate);
                allocate.clear(); //清空缓存区,并切换为读模式
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.nanoTime();
        System.out.println("code run time is:"+(end-start)/1000000+" ms");

    }

    private void buffTrans(){

        long start = System.nanoTime();

        File file = new File("tempData/old.txt");
        try(FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream("tempData/new.txt");
            FileChannel inChannel = fileInputStream.getChannel();
            FileChannel outChannel = fileOutputStream.getChannel();){

            ByteBuffer allocate = ByteBuffer.allocate((int)file.length());
            // inChannel.transferTo(0, allocate.limit(), outChannel);
            outChannel.transferFrom(inChannel, 0, allocate.limit());

        }catch(Exception e){

        }

        long end = System.nanoTime();
        System.out.println("code run time is:"+(end-start)/1000000+" ms");

    }

    private void buffsTrans(){

        long start1 = System.nanoTime();
        File file = new File("tempData/CentOS7.iso");
        try(FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream("tempData/new3.iso");
            FileChannel inChannel = fileInputStream.getChannel();
            FileChannel outChannel = fileOutputStream.getChannel();){
            
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024*100);
            while(inChannel.read(allocateDirect) != -1){
                allocateDirect.flip();
                outChannel.write(allocateDirect);
                allocateDirect.clear();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        long end1 = System.nanoTime();
        System.out.println("code1 run time is :"+(end1-start1)/1000000+" ms");

        // long start2 = System.nanoTime();
        // try(FileInputStream fileInputStream = new FileInputStream(file);
        //     FileOutputStream fileOutputStream = new FileOutputStream("tempData/new2.iso");
        //     FileChannel inChannel = fileInputStream.getChannel();
        //     FileChannel outChannel = fileOutputStream.getChannel();){
            
        //     ByteBuffer allocateDirect1 = ByteBuffer.allocateDirect(1024*10);
        //     ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(1024*10);
        //     ByteBuffer allocateDirect3 = ByteBuffer.allocateDirect(1024*10);

        //     ByteBuffer[] buffers = new ByteBuffer[]{allocateDirect1,allocateDirect2,allocateDirect3};
        //     while(inChannel.read(buffers) != -1){
        //         Arrays.stream(buffers).forEach(ByteBuffer::flip);
        //         outChannel.write(buffers);
        //         Arrays.stream(buffers).forEach(ByteBuffer::clear);
        //     }

        // } catch (Exception e) {
        //     // TODO: handle exception
        // }

        // long end2 = System.nanoTime();
        // System.out.println("code2 run time is :"+(end2-start2)/1000000+" ms");

    }

}
