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

	private getHoveredTable (mx: number, my: number): DesignLayoutObject | null {
		const tables = this.tables;
		const length = this.tables.length;

		for (let a = 0; a < length; a++) {
			const table = tables[a];
			const angle = table.rotation;
			const cos = Math.cos(-angle);
			const sin = Math.sin(-angle);

			const dx = mx - table.x;
			const dy = my - table.y;

			const localX = dx * cos - dy * sin;
			const localY = dx * sin + dy * cos;

			const halfW = table.w * 0.5;
			const halfH = table.h * 0.5;

			if (table.type === 'round') {
				if ((localX * localX) / (halfW * halfW) + (localY * localY) / (halfH * halfH) <= 1) return table;
			} else {
				if (localX <= halfW &&
				localX >= -halfW &&
				localY <= halfH &&
				localY >= -halfH) return table;
			}
		}

		return null;
	}

	public getHoveredObject (mx: number, my: number): DesignLayoutObject | null {
		const hoveredTable = this.getHoveredTable(mx, my);

		if (hoveredTable) return hoveredTable;

		return null;
	}
}
