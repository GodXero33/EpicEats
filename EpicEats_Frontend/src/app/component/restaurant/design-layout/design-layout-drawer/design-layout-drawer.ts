import { DesignLayout } from "./design-layout";
import { DesignLayoutObject } from "./design-layout-object/design-layout-object";
import { DesignLayoutTable } from "./design-layout-object/design-layout-table";
import { DesignLayoutVector } from "./design-layout-object/design-layout-vector";

export class DesignLayoutDrawer {
	public static readonly BG_COLOR: string = '#232323';
	public static readonly TABLE_COLOR: string = '#ffffff';
	public static readonly HIGHLIGHT_COLOR: string = '#ff0000';
	public static readonly SELECTION_COLOR: string = '#00ff00';

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
	private maxScale: number = 2;
	private minScale: number = 0.75;
	private scaleFact: number = 0.1;

	private isCanvasDragging: boolean = false;
	private isObjectDragging: boolean = false;
	private dragOffsetX: number = 0;
	private dragOffsetY: number = 0;

	private hoverObject: DesignLayoutObject | null = null;
	
	private dragObject: DesignLayoutObject | null = null;
	private dragRowObject: DesignLayoutVector | null = null;
	private lastActiveObject: DesignLayoutVector | null = null;
	private gridSnapSize: number = 20;
	private gridSnap: boolean = true;

	private deleteObject: DesignLayoutObject | null = null;

	private newObjectType: string | null = 'table-rect';

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

		this.ctx.shadowColor = '#000000';
		this.ctx.lineWidth = 3;
	}

	private mousedown (event: MouseEvent): void {
		if (event.button === 0) {
			this.updateDragOffset(event);

			if (this.dragObject && this.hoverObject == this.dragObject) {
				this.isObjectDragging = true;
				return;
			}

			this.isCanvasDragging = true;

			return;
		}

		if (event.button === 2) {
			if (this.hoverObject) this.deleteObject = this.hoverObject;
		}
	}

	private mousemove (event: MouseEvent): void {
		if (this.isObjectDragging && this.dragObject && this.dragRowObject) {
			this.dragRowObject.x += (event.x - this.dragOffsetX) / this.scale;
			this.dragRowObject.y += (event.y - this.dragOffsetY) / this.scale;

			if (this.gridSnap) {
				this.dragObject.x = Math.round(this.dragRowObject.x / this.gridSnapSize) * this.gridSnapSize;
				this.dragObject.y = Math.round(this.dragRowObject.y / this.gridSnapSize) * this.gridSnapSize;
			} else {
				this.dragObject.x = this.dragRowObject.x / this.gridSnapSize * this.gridSnapSize;
				this.dragObject.y = this.dragRowObject.y / this.gridSnapSize * this.gridSnapSize;
			}

			this.updateDragOffset(event);

			return;
		}

		if (this.isCanvasDragging) {
			this.translateX += event.x - this.dragOffsetX;
			this.translateY += event.y - this.dragOffsetY;
			
			this.updateDragOffset(event);

			return;
		}

		const [mrx, mry] = this.getRelativeMousePosition(event);
		this.hoverObject = this.layout.getHoveredObject(mrx, mry);
	}

	private mouseup (event: MouseEvent): void {
		this.isCanvasDragging = false;
		this.isObjectDragging = false;

		if (!this.hoverObject) {
			this.dragObject = null;
			this.dragRowObject = null;
		}

		if (event.button === 0) {
			if (this.hoverObject) {
				this.dragObject = this.hoverObject;
				this.dragRowObject = new DesignLayoutVector(this.dragObject.x, this.dragObject.y);
				this.lastActiveObject = this.hoverObject;

				return;
			}

			if (this.newObjectType) {
				this.dragObject = this.addNewObject(...this.getRelativeMousePosition(event) as [number, number]);
				this.hoverObject = this.dragObject;

				if (this.dragObject) this.dragRowObject = new DesignLayoutVector(this.dragObject.x, this.dragObject.y);
			}

			return;
		}

		if (event.button === 2) {
			if (this.deleteObject && this.deleteObject == this.hoverObject) {
				this.layout.delete(this.deleteObject);
				this.deleteObject = null;
				this.hoverObject = null;
				this.dragObject = null;
			}
		}
	}

	private wheel (event: WheelEvent): void {
		this.scale += Math.sign(event.deltaY) * this.scaleFact;

		if (this.scale < this.minScale) this.scale = this.minScale;
		if (this.scale > this.maxScale) this.scale = this.maxScale;

		const [mrx, mry] = this.getRelativeMousePosition(event);
		this.hoverObject = this.layout.getHoveredObject(mrx, mry);
	}

	private addNewObject (mx: number, my: number): DesignLayoutObject | null {
		if (this.gridSnap) {
			mx = Math.round(mx / this.gridSnapSize) * this.gridSnapSize;
			my = Math.round(my / this.gridSnapSize) * this.gridSnapSize;
		}

		if (this.newObjectType === 'table-rect') {
			const newObject: DesignLayoutObject =
				this.lastActiveObject instanceof DesignLayoutTable &&
				this.lastActiveObject.type === 'rect' ?
					new DesignLayoutTable(mx, my, this.lastActiveObject.w, this.lastActiveObject.h, this.lastActiveObject.rotation, 'rect') :
					new DesignLayoutTable(mx, my, 50, 50, 0, 'rect');

			this.layout.add(newObject);

			return newObject;
		}

		if (this.newObjectType === 'table-round') {
			const newObject: DesignLayoutObject =
				this.lastActiveObject instanceof DesignLayoutTable &&
				this.lastActiveObject.type === 'round' ?
					new DesignLayoutTable(mx, my, this.lastActiveObject.w, this.lastActiveObject.h, this.lastActiveObject.rotation, 'round') :
					new DesignLayoutTable(mx, my, 50, 50, 0, 'round');

			this.layout.add(newObject);

			return newObject;
		}

		return null;
	}

	private updateDragOffset (event: MouseEvent): void {
		this.dragOffsetX = event.x;
		this.dragOffsetY = event.y;
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

		if (this.hoverObject && this.hoverObject !== this.dragObject) this.hoverObject.drawOutline(this.ctx, DesignLayoutDrawer.HIGHLIGHT_COLOR);
		if (this.dragObject) this.dragObject.drawOutline(this.ctx, DesignLayoutDrawer.SELECTION_COLOR);

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

	public setSnap (snap: boolean): void {
		this.gridSnap = snap;
	}
}
