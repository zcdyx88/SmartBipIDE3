package test;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;
public class UMLClassFigure extends Figure {
public static Color classColor = new Color(null,255,255,206);
//属性子图形
private CompartmentFigure attributeFigure = new CompartmentFigure();
//方法子图形
private CompartmentFigure methodFigure = new CompartmentFigure();
public UMLClassFigure(Label name) {
ToolbarLayout layout = new ToolbarLayout();
//设置布局管理器
setLayoutManager(layout);
//设置图形的边框
setBorder(new LineBorder(ColorConstants.black,1));
//设置背景色
setBackgroundColor(classColor);
//设置图形是否透明
setOpaque(true);
//添加名字的标签
add(name);
//添加属性的图形
add(attributeFigure);
//添加方法的图形
add(methodFigure);
}
public CompartmentFigure getAttributesCompartment() {
return attributeFigure;
}
public CompartmentFigure getMethodsCompartment() {
return methodFigure;
}
}
