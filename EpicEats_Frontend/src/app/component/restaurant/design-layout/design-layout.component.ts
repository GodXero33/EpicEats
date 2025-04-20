import { Component, ElementRef, OnDestroy, ViewChild } from '@angular/core';
import { DesignLayoutDrawer } from './design-layout-drawer';
import { ApiService } from '../../../service/api.service';

@Component({
  selector: 'app-design-layout',
  imports: [],
  templateUrl: './design-layout.component.html',
  styleUrl: './design-layout.component.css'
})
export class DesignLayoutComponent implements OnDestroy {
  private designLayoutDrawer: DesignLayoutDrawer = new DesignLayoutDrawer();
  private canvasRef!: ElementRef;
  
  @ViewChild('canvas') set _canvas (reference: ElementRef) {
    this.canvasRef = reference;

    this.loadLayout();
  }

  constructor (private apiService: ApiService) {}
  
  public ngOnDestroy (): void {
    this.designLayoutDrawer.removeEvents();
    this.designLayoutDrawer.saveStatus();
  }

  private loadLayout (): void {
    this.apiService.get('/restaurant/layout').subscribe({
      next: (response) => {
        localStorage.setItem('design-layout-data', response as string);
        this.designLayoutDrawer.setCanvas(this.canvasRef.nativeElement as HTMLCanvasElement);
      },
      error: (error) => {
        console.error(error.message);
      }
    });
  }

  public saveData (): void {
    this.apiService.post('/restaurant/layout', this.designLayoutDrawer.statusToString()).subscribe({
      next: () => {
        alert('Layout saved');
      },
      error: (error) => {
        alert('Layout not saved');
        console.error(error.message);
      }
    });
  }
}
