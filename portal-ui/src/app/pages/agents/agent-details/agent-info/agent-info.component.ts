import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {TreeModule, TreeNodeSelectEvent} from "primeng/tree";
import {TreeNode} from "primeng/api";
import {AgentsService} from "../../agents.service";
import {FsNode} from "../../../../model/agents";
import {AsyncPipe, JsonPipe, NgForOf} from "@angular/common";
import {Observable, Subject, takeUntil} from "rxjs";
import {SplitterModule} from "primeng/splitter";
import {ScrollPanelModule} from "primeng/scrollpanel";
import {ToolbarModule} from "primeng/toolbar";
import {SpeedDialModule} from "primeng/speeddial";
import {MeterGroupModule} from "primeng/metergroup";
import {CardModule} from "primeng/card";
import {fetchEventSource} from "@microsoft/fetch-event-source";
import {ActivatedRoute} from "@angular/router";

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
    CardModule
  ],
  templateUrl: './agent-info.component.html',
  styleUrl: './agent-info.component.scss'
})
export class AgentInfoComponent implements OnInit, OnDestroy {

  private unsubscribe$: Subject<any> = new Subject<any>()
  protected sseSubscription$!: Observable<Array<any>>

  instanceId: string = ""

  files!: TreeNode[];
  selectedFile!: FsNode | null;
  filecontent: string[] = [];
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

nodeSelect($event: TreeNodeSelectEvent) {
    console.log($event);
    const selectedItem = $event.node.data as FsNode;

    this.selectedFile = null;

    if (!selectedItem.directory) {

      this.selectedFile = selectedItem;

      this.filecontent = [];
      console.log("selected file:", this.selectedFile);

      // this.sseSubscription$ = this.agentService.getSSE("http://localhost:8083/fs/filestream?filepath=" + encodeURIComponent(this.selectedFile.path))
      //   .pipe(takeUntil(this.unsubscribe$))


      this.agentService.getFileEventSource(this.instanceId, this.selectedFile.path,
        data => {
          this.filecontent.push(data);
        }

        )




      //=====================================================================
      //  fetch("http://localhost:8083/fs/filestream?filepath=" + encodeURIComponent(this.selectedFile.path))
      //   .then(response => {
      //
      //     if (!response.body || !response.ok) {
      //       throw new Error(`Response status: ${response.status}`);
      //     }
      //
      //     const reader = response.body.getReader();
      //     return new ReadableStream({
      //       start(controller) {
      //         function push() {
      //           reader.read().then(({done, value}) => {
      //             if (done) {
      //               controller.close();
      //               return;
      //             }
      //             controller.enqueue(value);
      //             push();
      //           });
      //         }
      //         push();
      //       }
      //     });
      //   })
      //   .then(stream => {
      //     //const jsonStream = new TextDecoder().decodeStream(stream);
      //     const reader = stream.pipeThrough(new TextDecoderStream()).getReader();
      //
      //     let  processResult = (result: ReadableStreamReadResult<any>): Promise<void> =>  {
      //       if (result.done) {
      //         console.log('Stream processing complete');
      //         console.log("Result",this.filecontent)
      //         return Promise.resolve();
      //       }
      //       if(result.value) {
      //         this.filecontent.push(result.value);
      //       }
      //       return reader.read().then( processResult);
      //     }
      //
      //     //let self = this;
      //
      //     return reader.read().then(
      //       processResult
      //     );
      //
      //   })
      //
      //   .catch(error => {
      //     console.error('Error streaming data:', error);
      //   });

       //===================================================================

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
