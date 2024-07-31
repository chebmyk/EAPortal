export interface Agent {
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


export interface FsNode {
  name: string
  isDirectory: boolean
  path: string
  children: FsNode[]
}
