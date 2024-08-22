export interface Agent {
  host: string
  instanceId: string
  metadata: any
  port: number
  scheme: number
  secure: boolean
  serviceId: string
  serviceInstance: ServiceInstance
  uri: string
}


export interface ServiceInstance {
  name: string
  id: string
  address: string
  port: number
  sslPort: number
  payload: Payload
  registrationTimeUTC: number
  serviceType: string
  uriSpec: UriSpec
  enabled: boolean
}

export interface Payload {
  "@class": string
  id: string
  name: string
  metadata: any
}


export interface UriSpec {
  parts: Part[]
}

export interface Part {
  value: string
  variable: boolean
}
