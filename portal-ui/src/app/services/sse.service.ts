import {Injectable, NgZone} from '@angular/core';
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SseService {

  constructor(
    private _zone: NgZone
  ) { }

  getServerEvents(url: string): Observable<any> {
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
}
