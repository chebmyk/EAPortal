import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {TreeModule, TreeNodeSelectEvent} from "primeng/tree";
import {TreeNode} from "primeng/api";
import {AgentsService} from "../../agents.service";
import {AsyncPipe, JsonPipe, NgForOf, NgIf} from "@angular/common";
import {BehaviorSubject, Subject} from "rxjs";
import {SplitterModule} from "primeng/splitter";
import {ScrollPanelModule} from "primeng/scrollpanel";
import {ToolbarModule} from "primeng/toolbar";
import {SpeedDialModule} from "primeng/speeddial";
import {MeterGroupModule} from "primeng/metergroup";
import {CardModule} from "primeng/card";
import {ActivatedRoute} from "@angular/router";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {FileUploadModule} from "primeng/fileupload";
import {ChipModule} from "primeng/chip";
import {FsAttributes, FsNode} from "../../../../model/file-system";
import {FileMetadataComponent} from "../../../../components/file-metadata/file-metadata.component";

@Component({
  selector: 'app-agent-info',
  standalone: true,
  imports: [
    TreeModule,
    JsonPipe,
    NgForOf,
    AsyncPipe,
    SplitterModule,
    ScrollPanelModule,
    ToolbarModule,
    SpeedDialModule,
    MeterGroupModule,
    CardModule,
    NgIf,
    ProgressSpinnerModule,
    FileUploadModule,
    ChipModule,
    FileMetadataComponent
  ],
  templateUrl: './agent-info.component.html',
  styleUrl: './agent-info.component.scss'
})
export class AgentInfoComponent implements OnInit, OnDestroy {

  fileMetaData$: BehaviorSubject<FsAttributes|undefined> = new BehaviorSubject<FsAttributes|undefined>(undefined);
  instanceId: string = ""
  files!: TreeNode[];
  selectedFile!: FsNode | null;
  filecontent: string[] = [];
  fileLoading: boolean = false;

  fileMenu  = [
    {
      icon: 'pi pi-save',
      routerLink: ['/fileupload']
    }
  ];
  messages=  [
    { label: 'Errors',icon: 'pi-exclamation-triangle',  color: 'rgb(246,29,0)', value: 2 },
    { label: 'Warnings', icon:'pi-exclamation-triangle', color: 'rgba(236,159,15,0.89)', value: 5 },
    { label: 'Info', icon:'pi-exclamation-triangle', color: 'rgba(15,111,236,0.89)', value: 20 }
  ];

  private unsubscribe$: Subject<any> = new Subject<any>()

  constructor(
    private agentService: AgentsService,
    private changeDetector: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute
  ) {
  }


  ngOnInit() {
    this.instanceId = this.activatedRoute.snapshot.params['id'];
    this.agentService.getFileTree(this.instanceId)
      .subscribe(node => {
        this.files= [this.mapNodeToTreeNode(node)];
        this.changeDetector.detectChanges();
      });
  }


  mapNodeToTreeNode(node: FsNode): TreeNode {
    const treeNode: TreeNode = {
      key: node.path,
      label: node.name,
      data: node,
      icon: node.directory? 'pi pi-folder text-yellow-500':'pi pi-file text-blue-500',
      children: node.children ? node.children.map(n => this.mapNodeToTreeNode(n)) : []
    };
    return treeNode;
  }



  onFileSelect($event: TreeNodeSelectEvent) {

    const selectedItem = $event.node.data as FsNode;
    this.selectedFile = null;

    if (!selectedItem.directory) {

      this.selectedFile = selectedItem;
      this.filecontent = [];


      this.agentService.getFileMetaData(this.instanceId, this.selectedFile.path)
        .subscribe( {
            next: attributes => {
              this.fileMetaData$.next(attributes);
            },
            error: error => {
              console.log("Error loading file attributes");
            }
          }
        )


      this.agentService.getFileContent(
          this.instanceId,
          this.selectedFile.path,
          data => {
            this.fileLoading=true;
            this.filecontent.push(data);
          },
          error => {
            this.fileLoading=false;
          },
          () => {
            this.fileLoading=false;
          }
        )
    }
  }


  ngOnDestroy(): void {
    if (this.unsubscribe$) {
      this.unsubscribe$.next(null);
      this.unsubscribe$.complete();
    }
  }
}
