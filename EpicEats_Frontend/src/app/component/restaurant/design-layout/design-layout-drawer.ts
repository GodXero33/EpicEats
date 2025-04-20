export class DesignLayoutDrawer {
	private canvas!: HTMLCanvasElement;
	private ctx!: CanvasRenderingContext2D;
	private width: number = 0;
	private height: number = 0;
	private eventAdded: boolean = false;
	private eventFuncs: Map<String, any> = new Map();
	private bgColor: string = '#232323';

	public setCanvas (canvas: HTMLCanvasElement): void {
		this.canvas = canvas;

		const ctx: CanvasRenderingContext2D | null = canvas.getContext('2d');

		if (!ctx) {
			alert('Failed to load 2D context');
			return;
		}

		this.ctx = ctx;

		const loadedData: string | null = localStorage.getItem('design-layout-data');

		if (loadedData) this.loadData(loadedData);

		this.resize();
		this.initEvents();
		this.animate();
	}

	private loadData (loadedData: string) {
		console.log(JSON.parse(loadedData));
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

		this.ctx.fillStyle = '#f00';
		this.ctx.fillRect(-20, -20, 40, 40);

		this.ctx.setTransform(transform);
	}

	private update () {}

	private animate () {
		this.update();
		this.draw();

		requestAnimationFrame(this.animate.bind(this));
	}

	public saveStatus (): void {
		localStorage.setItem('design-layout-data', this.statusToString());
	}

	public statusToString (): string {
		return JSON.stringify({
			name: 'shan'
		}, null, 2);
	}
}
