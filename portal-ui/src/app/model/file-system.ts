export interface FsNode {
  name: string
  directory: boolean
  path: string
  attributes?: FsAttributes
  children: FsNode[]
}


export interface FsAttributes {
  lastModifiedTime: Date;
  lastAccessTime: Date;
  creationTime: Date;
  regularFile: boolean;
  directory: boolean;
  symbolicLink: boolean;
  size: number;
}

