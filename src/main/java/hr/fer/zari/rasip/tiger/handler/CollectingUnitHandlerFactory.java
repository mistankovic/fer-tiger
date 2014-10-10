package hr.fer.zari.rasip.tiger.handler;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;

public class CollectingUnitHandlerFactory {

	public static CollectingUnitHandler handler(CollectingUnit cu) {
		if(cu == null || cu.getType() == null){
			return null;
		}
		String type = cu.getType().getName();

		switch (type) {
		case "GSN":
			return new GSNHandler();
		default:
			return null;
		}
	}
}
