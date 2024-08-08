import {Injectable, NgZone} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Agent, FsNode} from "../../model/agents";
import {async, BehaviorSubject, from, map, Observable, scan} from "rxjs";
import {fetchEventSource} from "@microsoft/fetch-event-source";

@Injectable({
  providedIn: 'root'
})
export class AgentsService {

  eventStream$ = new BehaviorSubject([]);
  eventSource!: EventSource;

  constructor(
    private http: HttpClient,
    private _zone: NgZone
  ) { }

  getAgents(): Observable<Agent[]> {
    return  this.http.get<Agent[]>(`http://localhost:8083/zookeeper/instance`);

    // return  this.http.get<Agent[]>(`${config.apiUrl}/zookeeper/instance`);
  }

  getAgent(id: string): Observable<Agent> {
    return  this.http.get<any>(`http://localhost:8083/zookeeper/instance/${id}`)
      // .pipe(
      //   map(i => i["serviceInstance"]
      //   )
      // )
      ;
    // return  this.http.get<Agent[]>(`${config.apiUrl}/zookeeper/instance`);
  }

  getFileTree(id: string): Observable<FsNode> {
    return  this.http.get<FsNode>(`http://localhost:8083/fs/${id}/filetree`);

    // return  this.http.get<Agent[]>(`${config.apiUrl}/zookeeper/instance`);
  }

//
  getSSE(url: string): Observable<any> {
    return new Observable(observer => {
      let eventSource = new EventSource(url);

      // eventSource.addEventListener(topic, event => {
      //   observer.next(event);
      // })
      // return () => eventSource.close();

      eventSource.onmessage = event => {
        this._zone.run(() => {
          observer.next(event);
        });
      };

      eventSource.onerror = error => {
        this._zone.run(() => {
          console.log("EventSource error", error)
          observer.error(error);
          eventSource.close();
        });
      };

      eventSource.onopen = () => {
        console.log("SSE Connection opened")
      }

      return () => eventSource.close();
    })

  }



  getFileEventSource = (id: string , filepath: string, onEvent: (data:any) => void) => {
      fetchEventSource(`http://localhost:8083/fs/${id}/filestream`, {
      method: 'POST',
      headers: {
       'Content-Type': 'application/json',
      },
        openWhenHidden: true,
        body: JSON.stringify({ "file": filepath }),
        onopen(response): Promise<void>  {
          console.log("onOpen fetch", response);
          return Promise.resolve();
        },
        onmessage(message)  {
          console.log("onmessage", message)
          onEvent(message.data);
        },
        onerror(error) {
          console.log("Error Fetch", error)
        },
        onclose() {
          console.log("Fetch Closed")
        }
    });
  }
}
