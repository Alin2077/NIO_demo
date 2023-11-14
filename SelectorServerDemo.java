import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorServerDemo {
    
    public static void main(String[] args) {
        
        new SelectorServerDemo().server();

    }

    private void server(){

        try (ServerSocketChannel open = ServerSocketChannel.open();) {  //打开接收网络io的通道

            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);
            open.bind(inetSocketAddress);  //绑定ip端口
            open.configureBlocking(false); //设置为非阻塞状态

            Selector selector = Selector.open(); //打开选择器
            open.register(selector, SelectionKey.OP_ACCEPT); //注册到选择器，并监听连接事件

            while (true) {  //循环等待

                if(selector.select(3000) == 0){
                    System.out.println("already 3s no event");
                    continue;
                }

               Set<SelectionKey> selectedKeys = selector.selectedKeys(); //如果有事件，获取事件
               Iterator<SelectionKey> iterator = selectedKeys.iterator();

               while (iterator.hasNext()) {

                    SelectionKey next = iterator.next();

                    if(next.isAcceptable()){ //如果是连接事件
                       
                        SocketChannel accept = open.accept(); //先接收连接
                        accept.configureBlocking(false); //将传入的连接通道设置为非阻塞
                        accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024)); //将连接的通道绑定到选择器，监听读事件
                        
                    }

                    if(next.isReadable()){ //如果是读事件

                        SocketChannel socketChannel = (SocketChannel)next.channel();//获取到事件绑定的通道
                        ByteBuffer buffer = (ByteBuffer)next.attachment(); //获取到事件绑定的ByteBuffer
                        socketChannel.read(buffer); //读取数据
                        System.out.println("from "+socketChannel.getRemoteAddress()+" :"+new String(buffer.array()));

                    }

                    iterator.remove(); //删除已处理的事件
               }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
