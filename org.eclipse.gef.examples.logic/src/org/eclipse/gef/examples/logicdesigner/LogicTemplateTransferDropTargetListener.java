package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.Transfer;

/**
 * This listener handles logic templates that are dropped onto
 * the logic editor.
 * 
 * @author Eric Bordeau
 */
public class LogicTemplateTransferDropTargetListener
	extends TemplateTransferDropTargetListener 
{

public LogicTemplateTransferDropTargetListener(EditPartViewer viewer) {
	super(viewer);
}

public LogicTemplateTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

protected CreateRequest.Factory getFactory(Object template) {
	if (template instanceof String)
		return new LogicTemplateFactory((String)template);
	return null;
}



class LogicTemplateFactory implements CreateRequest.Factory {
	
	private String template;
	
	public LogicTemplateFactory(String str) {
		template = str;
	}
	
	protected void connect(LogicSubpart part1, String conn1, LogicSubpart part2, String conn2) {
		Wire wire = new Wire();
		wire.setSource(part1);
		wire.setSourceTerminal(conn1);
		wire.setTarget(part2);
		wire.setTargetTerminal(conn2);
		wire.attachSource();
		wire.attachTarget();
	}
	
	public Circuit createFullAdder() {
		final Gate or;
		final Circuit circuit, circuit1, circuit2;
	
		circuit1 = createHalfAdder();
		circuit2 = createHalfAdder();
		circuit1.setLocation(new Point(2,10));
		circuit2.setLocation(new Point(38,90));
	
		circuit= new Circuit();
		circuit.setSize(new Dimension (120,216));
		or = new OrGate();
		or.setLocation(new Point(22,162));
	
		circuit.addChild(circuit1);
		circuit.addChild(circuit2);
	
		connect(circuit, circuit.TERMINALS_OUT[0], circuit1, circuit1.TERMINALS_IN[0]);
		connect(circuit, circuit.TERMINALS_OUT[2], circuit1, circuit1.TERMINALS_IN[3]);
		connect(circuit, circuit.TERMINALS_OUT[3], circuit2, circuit2.TERMINALS_IN[3]);
		connect(circuit1,circuit1.TERMINALS_OUT[7],circuit2, circuit2.TERMINALS_IN[0]);
	
		circuit.addChild(or);
		connect(or, or.TERMINAL_OUT, circuit, circuit.TERMINALS_IN[4]);
		connect(circuit1, circuit1.TERMINALS_OUT[4], or, or.TERMINAL_A);
		connect(circuit2, circuit2.TERMINALS_OUT[4], or, or.TERMINAL_B);
		connect(circuit2, circuit2.TERMINALS_OUT[7], circuit, circuit.TERMINALS_IN[7]);
	
		return circuit;
	}

	public Circuit createHalfAdder() {
		Gate and, xor;
		Circuit circuit;
	
		circuit = new Circuit();
		circuit.setSize(new Dimension(60,70));
		and = new AndGate();
		and.setLocation(new Point(2,12));
		xor = new XORGate();
		xor.setLocation(new Point(22,12));
	
		circuit.addChild(xor);
		circuit.addChild(and);
	
		connect(circuit, circuit.TERMINALS_OUT[0], and, and.TERMINAL_A);
		connect(circuit, circuit.TERMINALS_OUT[3], and, and.TERMINAL_B);
		connect(circuit, circuit.TERMINALS_OUT[0], xor, xor.TERMINAL_A);
		connect(circuit, circuit.TERMINALS_OUT[3], xor, xor.TERMINAL_B);
	
		connect(and, and.TERMINAL_OUT, circuit, circuit.TERMINALS_IN[4]);
		connect(xor, xor.TERMINAL_OUT, circuit, circuit.TERMINALS_IN[7]);
		return circuit;
	}
	
	public Object getNewObject() {
		if (LogicPlugin.TEMPLATE_AND_GATE.equals(template))
			return new AndGate();
		if (LogicPlugin.TEMPLATE_CIRCUIT.equals(template))
			return new Circuit();
		if (LogicPlugin.TEMPLATE_FLOW_CONTAINER.equals(template))
			return new LogicFlowContainer();
		if (LogicPlugin.TEMPLATE_FULL_ADDER.equals(template))
			return createFullAdder();
		if (LogicPlugin.TEMPLATE_GROUND.equals(template))
			return new GroundOutput();
		if (LogicPlugin.TEMPLATE_HALF_ADDER.equals(template))
			return createHalfAdder();
		if (LogicPlugin.TEMPLATE_LED.equals(template))
			return new LED();
		if (LogicPlugin.TEMPLATE_LIVE_OUTPUT.equals(template))
			return new LiveOutput();
		if (LogicPlugin.TEMPLATE_LOGIC_LABEL.equals(template))
			return new LogicLabel();
		if (LogicPlugin.TEMPLATE_OR_GATE.equals(template))
			return new OrGate();
		if (LogicPlugin.TEMPLATE_XOR_GATE.equals(template))
			return new XORGate();
		
		return null;
	}
	
	public Object getObjectType() {
		return template;
	}
}

}
