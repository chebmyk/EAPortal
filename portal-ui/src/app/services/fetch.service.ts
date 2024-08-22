import { Injectable } from '@angular/core';
import {fetchEventSource} from "@microsoft/fetch-event-source";

@Injectable({
  providedIn: 'root'
})
export class FetchService {

  constructor() { }

  post = (url: string, body: any,
                        onEvent: (data:any) => void,
                        onError?: ((error: any) => void),
                        onComplete?: () => void,

  ) => {
    fetchEventSource(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      openWhenHidden: true,
      body: JSON.stringify(body),
      onopen(response): Promise<void>  {
        return Promise.resolve();
      },
      onmessage(message)  {
        onEvent(message.data);
      },
      onerror(error) {
        console.log("Error Fetch", error)
        onError?.(error);
      },
      onclose() {
        onComplete?.();
      }
    });
  }

  get = (url: string,
          onEvent: (data:any) => void,
          onError?: ((error: any) => void),
          onComplete?: () => void,

  ) => {
    fetchEventSource(url, {
      openWhenHidden: true,
      onopen(response): Promise<void>  {
        return Promise.resolve();
      },
      onmessage(message)  {
        onEvent(message.data);
      },
      onerror(error) {
        console.log("Error Fetch", error)
        onError?.(error);
      },
      onclose() {
        onComplete?.();
      }
    });
  }

}
