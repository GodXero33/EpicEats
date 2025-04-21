import { DesignLayout } from "./design-layout";
import { DesignLayoutObject } from "./design-layout-object/design-layout-object";

export class DesignLayoutDrawer {
	public static readonly BG_COLOR: string = '#232323';
	public static readonly TABLE_COLOR: string = '#ffffff';
	public static readonly HIGHLIGHT_COLOR: string = '#ff0000';

	private canvas!: HTMLCanvasElement;
	private ctx!: CanvasRenderingContext2D;
	private width: number = 0;
	private height: number = 0;
	private eventAdded: boolean = false;
	private eventFuncs: Map<String, any> = new Map();
	private layout: DesignLayout = new DesignLayout();

	private translateX: number = 0;
	private translateY: number = 0;

	private scale: number = 1;
	private maxScale: number = 4;
	private minScale: number = 0.75;
	private scaleFact: number = 0.1;

	private isCanvasDragging: boolean = false;
	private canvasDragOffsetX: number = 0;
	private canvasDragOffsetY: number = 0;

	private hoverObject: DesignLayoutObject | null = null;

	public setCanvas (canvas: HTMLCanvasElement): void {
		this.canvas = canvas;

		const ctx = canvas.getContext('2d');

		if (!ctx) {
			alert('Failed to load 2D context');
			return;
		}

		this.ctx = ctx;

		const loadedData = localStorage.getItem('design-layout-data');

		if (loadedData) this.layout.mapFromJSONString(loadedData);

		this.resize();
		this.initEvents();
		this.animate();
	}

	private initEvents (): void {
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

	public removeEvents (): void {
		this.eventAdded = false;

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

			return;
		}

		const [mrx, mry] = this.getRelativeMousePosition(event);
		this.hoverObject = this.layout.getHoveredObject(mrx, mry);
	}

	private mouseup (event: MouseEvent): void {
		this.isCanvasDragging = false;
	}

	private wheel (event: WheelEvent): void {
		this.scale += Math.sign(event.deltaY) * this.scaleFact;

		if (this.scale < this.minScale) this.scale = this.minScale;
		if (this.scale > this.maxScale) this.scale = this.maxScale;
	}

	private getRelativeMousePosition (event: MouseEvent): Array<number> {
		const canvasRect = this.canvas.getBoundingClientRect();

		return [
			(event.x - canvasRect.x - this.width * 0.5 - this.translateX) / this.scale,
			(event.y - canvasRect.y - this.height * 0.5 - this.translateY) / this.scale
		];
	}

	private draw (): void {
		this.ctx.fillStyle = DesignLayoutDrawer.BG_COLOR;
		this.ctx.fillRect(0, 0, this.width, this.height);

		const transform = this.ctx.getTransform();

		this.ctx.translate(this.width * 0.5, this.height * 0.5);
		this.ctx.translate(this.translateX, this.translateY);
		this.ctx.scale(this.scale, this.scale);
		this.layout.draw(this.ctx);

		if (this.hoverObject) this.hoverObject.drawOutline(this.ctx);

		this.ctx.fillStyle = '#f00';

		this.ctx.setTransform(transform);
	}

	private update (): void {}

	private animate (): void {
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
