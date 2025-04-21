import { DesignLayoutObject } from "./design-layout-object/design-layout-object";
import { DesignLayoutTable } from "./design-layout-object/design-layout-table";

export class DesignLayout {
	public tables: Array<DesignLayoutTable> = [];

	public mapFromJSONString (json: string): void {
		try {
			const jsonObj = JSON.parse(json);

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
		this.tables.forEach(table => table.draw(ctx));
	}

	public getHoveredObject (mx: number, my: number): DesignLayoutObject | null {
		const tables = this.tables;
		const length = this.tables.length;

		for (let a = 0; a < length; a++) {
			const table = tables[a];

			if (mx <= table.x + table.w * 0.5 &&
				mx >= table.x - table.w * 0.5 &&
				my <= table.y + table.h * 0.5 &&
				my >= table.y - table.h * 0.5) return table;
		}

		return null;
	}
}
