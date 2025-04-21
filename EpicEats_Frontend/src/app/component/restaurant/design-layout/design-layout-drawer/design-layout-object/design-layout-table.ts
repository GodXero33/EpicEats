import { DesignLayoutDrawer } from "../design-layout-drawer";
import { DesignLayoutObject } from "./design-layout-object";

export class DesignLayoutTable extends DesignLayoutObject {
	public type: string;

	constructor (x: number, y: number, w: number, h: number, rotation: number = 0, type: string = 'rect') {
		super(x, y, w, h, rotation);

		this.type = type;
	}

	public override draw (ctx: CanvasRenderingContext2D): void {
		ctx.fillStyle = DesignLayoutDrawer.TABLE_COLOR;

		ctx.save();
		ctx.translate(this.x, this.y);
		ctx.rotate(this.rotation);

		ctx.beginPath();

		this.type === 'round' ?
			ctx.ellipse(0, 0, this.w * 0.5, this.h * 0.5, 0, 0, Math.PI * 2, false) :
			ctx.rect(-this.w * 0.5, -this.w * 0.5, this.w, this.h);

		ctx.fill();
		ctx.restore();
	}

	public override drawOutline (ctx: CanvasRenderingContext2D): void {
		ctx.strokeStyle = DesignLayoutDrawer.HIGHLIGHT_COLOR;
		ctx.lineWidth = 3;

		ctx.save();
		ctx.translate(this.x, this.y);
		ctx.rotate(this.rotation);

		ctx.beginPath();

		this.type === 'round' ?
			ctx.ellipse(0, 0, this.w * 0.5, this.h * 0.5, 0, 0, Math.PI * 2, false) :
			ctx.rect(-this.w * 0.5, -this.w * 0.5, this.w, this.h);

		ctx.stroke();
		ctx.restore();
	}

	public static isValidTable (obj: any): boolean {
		return obj &&
			typeof obj.x === 'number' &&
			typeof obj.y === 'number' &&
			typeof obj.w === 'number' &&
			typeof obj.h === 'number' &&
			(obj.rotation === undefined || typeof obj.rotation === 'number') &&
			(obj.type === undefined || typeof obj.type === 'string');
	}
}
