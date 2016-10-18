package com.dc.bip.ide.gef.parts;

import org.eclipse.gef.commands.Command;

import com.dc.bip.ide.gef.model.AbstractConnectionModel;

public class DelecteConnectionCommand extends Command {
		private AbstractConnectionModel connection;
		
		@Override
		public void execute()
		{
			connection.detachSource();
			connection.detachTarget();
		}
		
		@Override
		public void undo()
		{
			connection.attachSource();
			connection.attachTarget();
		}

		public AbstractConnectionModel getConnection() {
			return connection;
		}

		public void setConnection(Object connection) {
			this.connection = (AbstractConnectionModel)connection;
		}		
}
