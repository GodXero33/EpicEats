class Table {
	public x: number;
	public y: number;
	public w: number;
	public h: number;
	public rotation: number;
	public color: string = '#ffffff';

	constructor (x: number, y: number, w: number, h: number, rotation: number = 0) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rotation = rotation;
	}

	public draw (ctx: CanvasRenderingContext2D) {
		ctx.fillStyle = this.color;
		
		ctx.save();
		ctx.translate(this.x, this.y);
		ctx.rotate(this.rotation);
		ctx.fillRect(-this.w * 0.5, -this.w * 0.5, this.w, this.h);
		ctx.restore();
	}

	public static isValidTable (obj: any): obj is {
		x: number,
		y: number,
		w: number,
		h: number,
		type?: string,
		rotation?: number
	} {
		return obj &&
			typeof obj.x === 'number' &&
			typeof obj.y === 'number' &&
			typeof obj.w === 'number' &&
			typeof obj.h === 'number' &&
			(obj.type === undefined || typeof obj.type === 'string') &&
			(obj.rotation === undefined || typeof obj.rotation === 'number');
	}
}

class RoundTable extends Table {
	constructor (x: number, y: number, w: number, h: number, rotation: number = 0) {
		super(x, y, w, h, rotation);
	}

	public override draw (ctx: CanvasRenderingContext2D) {
		ctx.fillStyle = this.color;

		ctx.save();
		ctx.translate(this.x, this.y);
		ctx.rotate(this.rotation);
		ctx.beginPath();
		ctx.ellipse(0, 0, this.w * 0.5, this.h * 0.5, 0, 0, Math.PI * 2, false);
		ctx.fill();
		ctx.restore();
	}
}

class Layout {
	public tables: Array<Table> = [];

	public mapFromJSONString (json: string) {
		try {
			const jsonObj: any = JSON.parse(json);

			if (jsonObj.tables && Array.isArray(jsonObj.tables)) {
				jsonObj.tables.forEach((table: any) => {
					if (Table.isValidTable(table)) this.tables.push(table.type === 'round' ?
						new RoundTable(
							table.x,
							table.y,
							table.w,
							table.h,
							table.rotation
						) :
						new Table(
							table.x,
							table.y,
							table.w,
							table.h,
							table.rotation
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

		const resize = () => {
			this.resize();
		}

		this.eventFuncs.set('resize', resize);

		window.addEventListener('resize', resize);
	}

	public removeEvents () {
		this.eventAdded = false;

		window.addEventListener('resize', this.eventFuncs.get('resize'));
		this.eventFuncs.clear();
	}

	private resize (): void {
		if (!this.canvas.parentElement) return;

		this.width = this.canvas.parentElement.offsetWidth;
		this.height = this.canvas.parentElement.offsetHeight;

		this.canvas.width = this.width;
		this.canvas.height = this.height;
	}

	private draw () {
		this.ctx.fillStyle = this.bgColor;
		this.ctx.fillRect(0, 0, this.width, this.height);

		const transform: DOMMatrix = this.ctx.getTransform();

		this.ctx.translate(this.width * 0.5, this.height * 0.5);
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
