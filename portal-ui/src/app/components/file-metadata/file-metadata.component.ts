import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FsAttributes, FsNode} from "../../model/file-system";
import {BehaviorSubject, Observable, Subject, takeUntil} from "rxjs";
import {NgIf} from "@angular/common";

@Component({
  selector: 'file-metadata',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './file-metadata.component.html',
  styleUrl: './file-metadata.component.scss'
})
export class FileMetadataComponent implements OnInit, OnDestroy{

  @Input()
  fsNode!: FsNode | null;

  @Input()
  fsNodeAttribute$!: BehaviorSubject<FsAttributes|undefined>

  private unsubscribe$: Subject<any> = new Subject<any>()

  constructor() {}

  ngOnInit() : void {
    console.log("FileMetadataComponent.ngOnInit", this.fsNode)
    if( !!this.fsNodeAttribute$) {
      this.fsNodeAttribute$.pipe(takeUntil(this.unsubscribe$))
        .subscribe(
          {
            next: attr => {
              if (!!this.fsNode && !!attr) {
                this.fsNode.attributes = attr;
              }
            },
            error: error => {
              console.log("Error loading file attributes")
            }
          }
        )
    } else {
      throw new Error('Attribute "fsNodeAttribute$" is required');
    }
  }


  convertSize(bytes: number|undefined) {
    if (bytes) {
      if (bytes === 0) return '0 Byte';
      const sizes = ['Bytes', 'Kb', 'Mb', 'Gb', 'Tb', 'Pb'];
      const i = Math.floor(Math.log(bytes)/Math.log(1024));

      return `${(bytes / Math.pow(1024, i)).toFixed(2)} ${sizes[i]}`;
    } else {
      return 'n/a'
    }

  }


  ngOnDestroy(): void {
    console.log("ngOnDestroy")
    if (this.unsubscribe$) {
      this.unsubscribe$.next(null);
      this.unsubscribe$.complete();
    }
  }
}
