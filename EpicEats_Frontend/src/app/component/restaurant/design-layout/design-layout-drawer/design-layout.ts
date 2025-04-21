import { DesignLayoutTable } from "./design-layout-object/design-layout-table";

export class DesignLayout {
	public tables: Array<DesignLayoutTable> = [];

	public mapFromJSONString (json: string) {
		try {
			const jsonObj: any = JSON.parse(json);

			if (jsonObj.tables && Array.isArray(jsonObj.tables)) {
				jsonObj.tables.forEach((table: any) => {
					if (DesignLayoutTable.isValidTable(table)) this.tables.push(new DesignLayoutTable(
						table.x,
						table.y,
						table.w,
						table.h,
						table.rotation,
						table.type
					));
				});
			}
		} catch (error) {
			console.error('Failed to map loaded string to a layout data:', error);
		}
	}

	public draw (ctx: CanvasRenderingContext2D) {
		this.tables.forEach((table: DesignLayoutTable) => table.draw(ctx));
	}
}
