export class DesignLayoutObject {
	public x: number;
	public y: number;
	public w: number;
	public h: number;
	public rotation: number;

	constructor (x: number, y: number, w: number, h: number, rotation: number = 0) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rotation = rotation;
	}

	public drawOutline (ctx: CanvasRenderingContext2D, color: string): void {}

	public draw (ctx: CanvasRenderingContext2D): void {}
}
