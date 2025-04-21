class Table {
	public x: number;
	public y: number;
	public w: number;
	public h: number;
	public rotation: number;
	public color: string = '#ffffff';
	public type: string;

	constructor (x: number, y: number, w: number, h: number, rotation: number = 0, type: string = 'rect') {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rotation = rotation;
		this.type = type;
	}

	public draw (ctx: CanvasRenderingContext2D) {
		ctx.fillStyle = this.color;
		
		ctx.save();
		ctx.translate(this.x, this.y);
		ctx.rotate(this.rotation);
		
		this.type === 'round' ? this.drawRound(ctx) : this.drawRect(ctx);

		ctx.restore();
	}

	public drawRect (ctx: CanvasRenderingContext2D) {
		ctx.fillRect(-this.w * 0.5, -this.w * 0.5, this.w, this.h);
	}

	public drawRound (ctx: CanvasRenderingContext2D) {
		ctx.beginPath();
		ctx.ellipse(0, 0, this.w * 0.5, this.h * 0.5, 0, 0, Math.PI * 2, false);
		ctx.fill();
	}

	public static isValidTable (obj: any): obj is {
		x: number,
		y: number,
		w: number,
		h: number,
		rotation?: number,
		type?: string
	} {
		return obj &&
			typeof obj.x === 'number' &&
			typeof obj.y === 'number' &&
			typeof obj.w === 'number' &&
			typeof obj.h === 'number' &&
			(obj.rotation === undefined || typeof obj.rotation === 'number') &&
			(obj.type === undefined || typeof obj.type === 'string');
	}
}

class Layout {
	public tables: Array<Table> = [];

	public mapFromJSONString (json: string) {
		try {
			const jsonObj: any = JSON.parse(json);

			if (jsonObj.tables && Array.isArray(jsonObj.tables)) {
				jsonObj.tables.forEach((table: any) => {
					if (Table.isValidTable(table)) this.tables.push(new Table(
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
		this.tables.forEach((table: Table) => table.draw(ctx));
	}
}

export class DesignLayoutDrawer {
	private canvas!: HTMLCanvasElement;
	private ctx!: CanvasRenderingContext2D;
	private width: number = 0;
	private height: number = 0;
	private eventAdded: boolean = false;
	private eventFuncs: Map<String, any> = new Map();
	private bgColor: string = '#232323';
	private layout: Layout = new Layout();

	private translateX: number = 0;
	private translateY: number = 0;

	private scale: number = 1;
	private maxScale: number = 6;
	private minScale: number = 0.2;
	private scaleFact: number = 0.1;

	private isCanvasDragging: boolean = false;
	private canvasDragOffsetX: number = 0;
	private canvasDragOffsetY: number = 0;

	public setCanvas (canvas: HTMLCanvasElement): void {
		this.canvas = canvas;

		const ctx: CanvasRenderingContext2D | null = canvas.getContext('2d');

		if (!ctx) {
			alert('Failed to load 2D context');
			return;
		}

		this.ctx = ctx;

		const loadedData: string | null = localStorage.getItem('design-layout-data');

		if (loadedData) this.layout.mapFromJSONString(loadedData);

		this.resize();
		this.initEvents();
		this.animate();
	}

	private initEvents () {
		if (this.eventAdded) return;

		this.eventAdded = true;

		const resize = () => this.resize();
		const mousedown = (event: MouseEvent) => {
			if (event.target != this.canvas) return;

			this.mousedown(event);
		};
		const contextmenu = (event: MouseEvent) => {
			if (event.target === this.canvas) event.preventDefault();
		};
		const mousemove = (event: MouseEvent) => this.mousemove(event);
		const mouseup = (event: MouseEvent) => this.mouseup(event);
		const wheel = (event: WheelEvent) => this.wheel(event);

		this.eventFuncs.set('resize', resize);
		this.eventFuncs.set('mousedown', mousedown);
		this.eventFuncs.set('contextmenu', contextmenu);
		this.eventFuncs.set('mousemove', mousemove);
		this.eventFuncs.set('mouseup', mouseup);
		this.eventFuncs.set('wheel', wheel);

		window.addEventListener('resize', resize);
		window.addEventListener('mousedown', mousedown);
		window.addEventListener('contextmenu', contextmenu);
		window.addEventListener('mousemove', mousemove);
		window.addEventListener('mouseup', mouseup);
		window.addEventListener('wheel', wheel);
	}

	public removeEvents () {
		this.eventAdded = false;

		const keys: MapIterator<String> = this.eventFuncs.keys();

		for (const [eventName, handler] of this.eventFuncs)
			window.removeEventListener(eventName as string, handler);

		this.eventFuncs.clear();
	}

	private resize (): void {
		if (!this.canvas.parentElement) return;

		this.width = this.canvas.parentElement.offsetWidth;
		this.height = this.canvas.parentElement.offsetHeight;

		this.canvas.width = this.width;
		this.canvas.height = this.height;
	}

	private mousedown (event: MouseEvent): void {
		if (event.button === 0) {
			this.isCanvasDragging = true;
			this.canvasDragOffsetX = event.x;
			this.canvasDragOffsetY = event.y;
		}
	}

	private mousemove (event: MouseEvent): void {
		if (this.isCanvasDragging) {
			this.translateX += event.x - this.canvasDragOffsetX;
			this.translateY += event.y - this.canvasDragOffsetY;
			
			this.canvasDragOffsetX = event.x;
			this.canvasDragOffsetY = event.y;
		}
	}

	private mouseup (event: MouseEvent): void {
		this.isCanvasDragging = false;
	}

	private wheel (event: WheelEvent): void {
		this.scale += Math.sign(event.deltaY) * this.scaleFact;

		if (this.scale < this.minScale) this.scale = this.minScale;
		if (this.scale > this.maxScale) this.scale = this.maxScale;
	}

	private draw () {
		this.ctx.fillStyle = this.bgColor;
		this.ctx.fillRect(0, 0, this.width, this.height);

		const transform: DOMMatrix = this.ctx.getTransform();

		this.ctx.translate(this.width * 0.5, this.height * 0.5);
		this.ctx.translate(this.translateX, this.translateY);
		this.ctx.scale(this.scale, this.scale);
		this.layout.draw(this.ctx);
		this.ctx.setTransform(transform);
	}

	private update () {}

	private animate () {
		this.update();
		this.draw();

		requestAnimationFrame(this.animate.bind(this));
	}

	public saveLayout (): void {
		localStorage.setItem('design-layout-data', this.layoutToString());
	}

	public layoutToString (): string {
		return JSON.stringify(this.layout, null, 2);
	}
}
