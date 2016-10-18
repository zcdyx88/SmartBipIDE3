package test;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
public class MuilFigure extends Figure {
//在Figure中添加标签(标签也是Figure)
private	Image typeIcon;
private  Figure figure = new Figure();

public MuilFigure(String fieldLabel, Image typeIcon) {
	this.typeIcon = typeIcon;
/*//设置背景色
setBackgroundColor(ColorConstants.tooltipBackground);
//设置前景色
setForegroundColor(ColorConstants.tooltipForeground);
//设置Figure的边框
setBorder(new LineBorder());
setOpaque(true);
//setLayoutManager(new ToolbarLayout());
*/
	XYLayout layout = new XYLayout();
	setLayoutManager(layout);

//figure.setBackgroundColor(ColorConstants.red);
//设置node1的大小和位置
figure.setLayoutManager(new ToolbarLayout());
add(figure);
layout.setConstraint(figure, new Rectangle(0,0,-1,-1));

}

public void addLabel(String tex)
{
	
      Label label = new Label();
	//设置label中文字的对齐方式
	label.setTextAlignment(PositionConstants.LEFT);
	//设置label的图标
	label.setIcon(typeIcon);
	//设置Figure的布局方式
	
	//把label添加到Figure中	
	label.setBorder(new LineBorder());
	//设置label的显示文字
	label.setText(tex);
	figure.add(label);
	
    Label label2 = new Label();
	//设置label中文字的对齐方式
	label2.setTextAlignment(PositionConstants.LEFT);
	//设置label的图标
	label2.setIcon(typeIcon);
	//设置Figure的布局方式
	
	//把label添加到Figure中	
	label2.setBorder(new LineBorder());
	label2.setBorder(new CompoundBorder(new LineBorder(),new MarginBorder(40)));
	//设置label的显示文字
	label2.setText(tex);
	figure.add(label2);
}
}
