package test;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
public class Draw2DTest {
public static void main(String args[]){
//新建Shell，Shell是Canvas的子类
Shell shell = new Shell();
shell.open();
shell.setText("Draw2d Hello World");
//添加LightweightSystem实例
LightweightSystem lws = new LightweightSystem(shell);

/*IFigure layer = new Figure();
XYLayOut xyLayOut = new */
//添加IFigure实例
MuilFigure parentFigure = new MuilFigure("test",new Image(Display.getDefault(),"icons/0.png"));
parentFigure.addLabel("label1");
parentFigure.addLabel("label2");
parentFigure.addLabel("label3");
//parentFigure.addRectangle();
//把IFigure添加到LightweightSystem中
lws.setContents(parentFigure);
Display display = Display.getDefault();
while (!shell.isDisposed ()) {
if (!display.readAndDispatch ())
display.sleep ();
}
}
}
