package test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
public class Connection {
public static void main(String args[]){
Shell shell = new Shell();
shell.setSize(350,350);
shell.open();
shell.setText("Demo 4");
LightweightSystem lws = new LightweightSystem(shell);
IFigure panel = new Figure();
lws.setContents(panel);
//创建两个四边形的图形实例
RectangleFigure
node1 = new RectangleFigure(),
node2 = new RectangleFigure();
//设置node1的背景色
node1.setBackgroundColor(ColorConstants.red);
//设置node1的大小和位置
node1.setBounds(new Rectangle(30, 30, 64, 36));
//设置node2的背景色
node2.setBackgroundColor(ColorConstants.blue);
//设置node2的大小和位置
node2.setBounds(new Rectangle(100, 100, 64, 36));
//创建一个连线的实例
PolylineConnection conn = new PolylineConnection();
//设置连线起点的锚点
conn.setSourceAnchor(new ChopboxAnchor(node1));
//设置连线目标的锚点
conn.setTargetAnchor(new ChopboxAnchor(node2));
//设置连线目标的装饰器
//conn.setTargetDecoration(new PolygonDecoration());
Label label = new Label("Midpoint");
label.setOpaque(true);
label.setBackgroundColor(ColorConstants.buttonLightest);
label.setBorder(new LineBorder());
//添加连线的Locator
conn.add(label, new MidpointLocator(conn, 0));
//在底层Figure中添加子Figure
panel.add(node1);
panel.add(node2);
panel.add(conn);
//添加node1拖动的监听器
new Dragger(node1);
//添加node2拖动的监听器
new Dragger(node2);
Display display = Display.getDefault();
while (!shell.isDisposed()) {
if (!display.readAndDispatch())
display.sleep ();
}
}
static class Dragger extends MouseMotionListener.Stub implements MouseListener {
public Dragger(IFigure figure){
figure.addMouseMotionListener(this);
figure.addMouseListener(this);
}
Point last;
public void mouseReleased(MouseEvent e){}
public void mouseClicked(MouseEvent e){}
public void mouseDoubleClicked(MouseEvent e){}
public void mousePressed(MouseEvent e){
last = e.getLocation();
}
public void mouseDragged(MouseEvent e){
Point p = e.getLocation();
Dimension delta = p.getDifference(last);
last = p;
Figure f = ((Figure)e.getSource());
//设置拖动的Figure的位置
f.setBounds(f.getBounds().getTranslated(delta.width, delta.height));
}
};
}
