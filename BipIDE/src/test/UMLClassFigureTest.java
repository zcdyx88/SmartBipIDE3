package test;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
public class UMLClassFigureTest {
	
public static void main(String args[])
{
Display d = new Display();
final Shell shell = new Shell(d);
shell.setSize(400, 400);
shell.setText("UMLClassFigure Test");
LightweightSystem lws = new LightweightSystem(shell);
//新建底层的图形
Figure contents = new Figure();
//设置底层图的布局方式为XYLayout
XYLayout contentsLayout = new XYLayout();
contents.setLayoutManager(contentsLayout);
Font classFont = new Font(null, "Arial", 12, SWT.BOLD);
//添加表的显示文本及显示的图标
Label classLabel1 = new Label("Table", new Image(d,
UMLClassFigureTest.class.getResourceAsStream("/icons/0.png")));
classLabel1.setFont(classFont);
Label classLabel2 = new Label("Column", new Image(d,
UMLClassFigureTest.class.getResourceAsStream("/icons/0.png")));
classLabel2.setFont(classFont);
//新建表和列的图形
final UMLClassFigure classFigure = new UMLClassFigure(classLabel1);
final UMLClassFigure classFigure2 = new UMLClassFigure(classLabel2);
//添加属性的显示文本及显示图标
Label attribute1 = new Label("columns: Column[]", new Image(d,
UMLClassFigure.class.getResourceAsStream("/icons/0.png")));
Label attribute2 = new Label("rows: Row[]", new Image(d,
UMLClassFigure.class.getResourceAsStream("/icons/0.png")));
Label attribute3 = new Label("columnID: int", new Image(d,
UMLClassFigure.class.getResourceAsStream("/icons/0.png")));
Label attribute4 = new Label("items: List", new Image(d,
UMLClassFigure.class.getResourceAsStream("/icons/0.png")));
//添加方法和属性的标签
classFigure.getAttributesCompartment().add(attribute1);
classFigure.getAttributesCompartment().add(attribute2);
classFigure2.getAttributesCompartment().add(attribute3);
classFigure2.getAttributesCompartment().add(attribute4);
Label method1 = new Label("getColumns(): Column[]", new Image(d,
UMLClassFigure.class.getResourceAsStream("/icons/0.png")));
Label method2 = new Label("getRows(): Row[]", new Image(d,
UMLClassFigure.class.getResourceAsStream("/icons/0.png")));
Label method3 = new Label("getColumnID(): int", new Image(d,
UMLClassFigure.class.getResourceAsStream("/icons/0.png")));
Label method4 = new Label("getItems(): List", new Image(d,
UMLClassFigure.class.getResourceAsStream("/icons/0.png")));
classFigure.getMethodsCompartment().add(method1);
classFigure.getMethodsCompartment().add(method2);
classFigure2.getMethodsCompartment().add(method3);
classFigure2.getMethodsCompartment().add(method4);
contentsLayout.setConstraint(classFigure, new Rectangle(10,10,-1,-1));
contentsLayout.setConstraint(classFigure2, new Rectangle(200, 200, -1, -1));
//新建连线
PolylineConnection c = new PolylineConnection();
//添加图形的锚点
ChopboxAnchor sourceAnchor = new ChopboxAnchor(classFigure);
ChopboxAnchor targetAnchor = new ChopboxAnchor(classFigure2);
c.setSourceAnchor(sourceAnchor);
c.setTargetAnchor(targetAnchor);
//添加连线的装饰器
PolygonDecoration decoration = new PolygonDecoration();
PointList decorationPointList = new PointList();
decorationPointList.addPoint(0,0);
decorationPointList.addPoint(-2,2);
decorationPointList.addPoint(-4,0);
decorationPointList.addPoint(-2,-2);
decoration.setTemplate(decorationPointList);
c.setSourceDecoration(decoration);
//添加连线的Locator
ConnectionEndpointLocator targetEndpointLocator =
new ConnectionEndpointLocator(c, true);
targetEndpointLocator.setVDistance(15);
Label targetMultiplicityLabel = new Label("1..*");
c.add(targetMultiplicityLabel, targetEndpointLocator);
//添加连线的Locator
ConnectionEndpointLocator sourceEndpointLocator =
new ConnectionEndpointLocator(c, false);
sourceEndpointLocator.setVDistance(15);
Label sourceMultiplicityLabel = new Label("1");
c.add(sourceMultiplicityLabel, sourceEndpointLocator);
//添加连线的Locator
ConnectionEndpointLocator relationshipLocator =
new ConnectionEndpointLocator(c,true);
relationshipLocator.setUDistance(10);
relationshipLocator.setVDistance(-20);
Label relationshipLabel = new Label("contains");
c.add(relationshipLabel,relationshipLocator);
//把表、列和连线(即关系)添加到底层图形上
contents.add(classFigure);
contents.add(classFigure2);
contents.add(c);
lws.setContents(contents);
shell.open();
while (!shell.isDisposed())
while (!d.readAndDispatch())
d.sleep();
}
}