import { NgModule } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CardModule } from 'primeng/card';
import { ToolbarModule } from 'primeng/toolbar';
import { SplitButtonModule } from 'primeng/splitbutton';
import { PaginatorModule } from 'primeng/paginator';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { FileUploadModule } from 'primeng/fileupload';
import { RatingModule } from 'primeng/rating';
import { FloatLabelModule } from 'primeng/floatlabel';
import { TooltipModule } from 'primeng/tooltip';
import { TagModule } from 'primeng/tag';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { DataViewModule } from 'primeng/dataview';
import { PanelModule } from 'primeng/panel';
import { DividerModule } from 'primeng/divider';

@NgModule({
  exports: [
    ButtonModule,
    TableModule,
    InputTextModule,
    InputNumberModule,
    CalendarModule,
    DropdownModule,
    DialogModule,
    ToastModule,
    ConfirmDialogModule,
    CardModule,
    ToolbarModule,
    SplitButtonModule,
    PaginatorModule,
    InputTextareaModule,
    FileUploadModule,
    RatingModule,
    FloatLabelModule,
    TooltipModule,
    TagModule,
    ProgressSpinnerModule,
    DataViewModule,
    PanelModule,
    DividerModule
  ]
})
export class PrimeNGModule { }
