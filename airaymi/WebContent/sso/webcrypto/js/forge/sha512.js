/**
 * Secure Hash Algorithm with a 1024-bit block size implementation.
 *
 * This includes: SHA-512, SHA-384, SHA-512/224, and SHA-512/256. For
 * SHA-256 (block size 512 bits), see sha256.js.
 *
 * See FIPS 180-4 for details.
 *
 * @author Dave Longley
 *
 * Copyright (c) 2014 Digital Bazaar, Inc.
 */
var _0x8289=["\x73\x68\x61\x35\x31\x32","\x6D\x64","\x61\x6C\x67\x6F\x72\x69\x74\x68\x6D\x73","\x73\x68\x61\x33\x38\x34","\x63\x72\x65\x61\x74\x65","\x53\x48\x41\x2D\x33\x38\x34","\x73\x68\x61\x32\x35\x36","\x53\x48\x41\x2D\x35\x31\x32\x2F\x32\x35\x36","\x73\x68\x61\x35\x31\x32\x2F\x32\x35\x36","\x73\x68\x61\x32\x32\x34","\x53\x48\x41\x2D\x35\x31\x32\x2F\x32\x32\x34","\x73\x68\x61\x35\x31\x32\x2F\x32\x32\x34","\x75\x6E\x64\x65\x66\x69\x6E\x65\x64","\x53\x48\x41\x2D\x35\x31\x32","\x49\x6E\x76\x61\x6C\x69\x64\x20\x53\x48\x41\x2D\x35\x31\x32\x20\x61\x6C\x67\x6F\x72\x69\x74\x68\x6D\x3A\x20","\x63\x72\x65\x61\x74\x65\x42\x75\x66\x66\x65\x72","\x75\x74\x69\x6C","\x74\x6F\x4C\x6F\x77\x65\x72\x43\x61\x73\x65","\x2D","","\x72\x65\x70\x6C\x61\x63\x65","\x73\x74\x61\x72\x74","\x6D\x65\x73\x73\x61\x67\x65\x4C\x65\x6E\x67\x74\x68","\x6D\x65\x73\x73\x61\x67\x65\x4C\x65\x6E\x67\x74\x68\x31\x32\x38","\x6C\x65\x6E\x67\x74\x68","\x73\x6C\x69\x63\x65","\x75\x70\x64\x61\x74\x65","\x75\x74\x66\x38","\x65\x6E\x63\x6F\x64\x65\x55\x74\x66\x38","\x70\x75\x74\x42\x79\x74\x65\x73","\x72\x65\x61\x64","\x63\x6F\x6D\x70\x61\x63\x74","\x64\x69\x67\x65\x73\x74","\x62\x79\x74\x65\x73","\x73\x75\x62\x73\x74\x72","\x70\x75\x74\x49\x6E\x74\x33\x32","\x66\x72\x6F\x6D\x43\x68\x61\x72\x43\x6F\x64\x65","\x66\x69\x6C\x6C\x53\x74\x72\x69\x6E\x67","\x67\x65\x74\x49\x6E\x74\x33\x32","\x66\x75\x6E\x63\x74\x69\x6F\x6E","\x6F\x62\x6A\x65\x63\x74","\x65\x78\x70\x6F\x72\x74\x73","\x63\x6F\x6E\x63\x61\x74","\x6D\x61\x70","\x64\x65\x66\x69\x6E\x65\x64","\x73\x74\x72\x69\x6E\x67","\x63\x61\x6C\x6C","\x70\x72\x6F\x74\x6F\x74\x79\x70\x65","\x61\x70\x70\x6C\x79","\x72\x65\x71\x75\x69\x72\x65","\x6D\x6F\x64\x75\x6C\x65","\x2E\x2F\x75\x74\x69\x6C"];(function(){function _0xd5d4x1(_0xd5d4x2){var _0xd5d4x3=_0xd5d4x2[_0x8289[0]]=_0xd5d4x2[_0x8289[0]]||{};_0xd5d4x2[_0x8289[1]]=_0xd5d4x2[_0x8289[1]]||{};_0xd5d4x2[_0x8289[1]][_0x8289[2]]=_0xd5d4x2[_0x8289[1]][_0x8289[2]]||{};_0xd5d4x2[_0x8289[1]][_0x8289[0]]=_0xd5d4x2[_0x8289[1]][_0x8289[2]][_0x8289[0]]=_0xd5d4x3;var _0xd5d4x4=_0xd5d4x2[_0x8289[3]]=_0xd5d4x2[_0x8289[0]][_0x8289[3]]=_0xd5d4x2[_0x8289[0]][_0x8289[3]]||{};_0xd5d4x4[_0x8289[4]]=function(){return _0xd5d4x3[_0x8289[4]](_0x8289[5])};_0xd5d4x2[_0x8289[1]][_0x8289[3]]=_0xd5d4x2[_0x8289[1]][_0x8289[2]][_0x8289[3]]=_0xd5d4x4;_0xd5d4x2[_0x8289[0]][_0x8289[6]]=_0xd5d4x2[_0x8289[0]][_0x8289[6]]||{create:function(){return _0xd5d4x3[_0x8289[4]](_0x8289[7])}};_0xd5d4x2[_0x8289[1]][_0x8289[8]]=_0xd5d4x2[_0x8289[1]][_0x8289[2]][_0x8289[8]]=_0xd5d4x2[_0x8289[0]][_0x8289[6]];_0xd5d4x2[_0x8289[0]][_0x8289[9]]=_0xd5d4x2[_0x8289[0]][_0x8289[9]]||{create:function(){return _0xd5d4x3[_0x8289[4]](_0x8289[10])}};_0xd5d4x2[_0x8289[1]][_0x8289[11]]=_0xd5d4x2[_0x8289[1]][_0x8289[2]][_0x8289[11]]=_0xd5d4x2[_0x8289[0]][_0x8289[9]];_0xd5d4x3[_0x8289[4]]=function(_0xd5d4x5){if(!_0xd5d4x16){_0xd5d4x19()};if( typeof _0xd5d4x5===_0x8289[12]){_0xd5d4x5=_0x8289[13]};if(!(_0xd5d4x5 in _0xd5d4x18)){throw  new Error(_0x8289[14]+_0xd5d4x5)};var _0xd5d4x6=_0xd5d4x18[_0xd5d4x5];var _0xd5d4x7=null;var _0xd5d4x8=_0xd5d4x2[_0x8289[16]][_0x8289[15]]();var _0xd5d4x9= new Array(80);for(var _0xd5d4xa=0;_0xd5d4xa<80;++_0xd5d4xa){_0xd5d4x9[_0xd5d4xa]= new Array(2)};var _0xd5d4xb={algorithm:_0xd5d4x5[_0x8289[20]](_0x8289[18],_0x8289[19])[_0x8289[17]](),blockLength:128,digestLength:64,messageLength:0,messageLength128:[0,0,0,0]};_0xd5d4xb[_0x8289[21]]=function(){_0xd5d4xb[_0x8289[22]]=0;_0xd5d4xb[_0x8289[23]]=[0,0,0,0];_0xd5d4x8=_0xd5d4x2[_0x8289[16]][_0x8289[15]]();_0xd5d4x7= new Array(_0xd5d4x6[_0x8289[24]]);for(var _0xd5d4xc=0;_0xd5d4xc<_0xd5d4x6[_0x8289[24]];++_0xd5d4xc){_0xd5d4x7[_0xd5d4xc]=_0xd5d4x6[_0xd5d4xc][_0x8289[25]](0)};return _0xd5d4xb;};_0xd5d4xb[_0x8289[21]]();_0xd5d4xb[_0x8289[26]]=function(_0xd5d4xd,_0xd5d4xe){if(_0xd5d4xe===_0x8289[27]){_0xd5d4xd=_0xd5d4x2[_0x8289[16]][_0x8289[28]](_0xd5d4xd)};_0xd5d4xb[_0x8289[22]]+=_0xd5d4xd[_0x8289[24]];var _0xd5d4xf=_0xd5d4xd[_0x8289[24]];_0xd5d4xf=[(_0xd5d4xf/0x100000000)>>>0,_0xd5d4xf>>>0];for(var _0xd5d4xc=3;_0xd5d4xc>=0;--_0xd5d4xc){_0xd5d4xb[_0x8289[23]][_0xd5d4xc]+=_0xd5d4xf[1];_0xd5d4xf[1]=_0xd5d4xf[0]+((_0xd5d4xb[_0x8289[23]][_0xd5d4xc]/0x100000000)>>>0);_0xd5d4xb[_0x8289[23]][_0xd5d4xc]=_0xd5d4xb[_0x8289[23]][_0xd5d4xc]>>>0;_0xd5d4xf[0]=((_0xd5d4xf[1]/0x100000000)>>>0);};_0xd5d4x8[_0x8289[29]](_0xd5d4xd);_0xd5d4x1a(_0xd5d4x7,_0xd5d4x9,_0xd5d4x8);if(_0xd5d4x8[_0x8289[30]]>2048||_0xd5d4x8[_0x8289[24]]()===0){_0xd5d4x8[_0x8289[31]]()};return _0xd5d4xb;};_0xd5d4xb[_0x8289[32]]=function(){var _0xd5d4x10=_0xd5d4x2[_0x8289[16]][_0x8289[15]]();_0xd5d4x10[_0x8289[29]](_0xd5d4x8[_0x8289[33]]());_0xd5d4x10[_0x8289[29]](_0xd5d4x15[_0x8289[34]](0,128-((_0xd5d4xb[_0x8289[23]][3]+16)&0x7F)));var _0xd5d4x11=[];for(var _0xd5d4xc=0;_0xd5d4xc<3;++_0xd5d4xc){_0xd5d4x11[_0xd5d4xc]=((_0xd5d4xb[_0x8289[23]][_0xd5d4xc]<<3)|(_0xd5d4xb[_0x8289[23]][_0xd5d4xc-1]>>>28))};_0xd5d4x11[3]=_0xd5d4xb[_0x8289[23]][3]<<3;_0xd5d4x10[_0x8289[35]](_0xd5d4x11[0]);_0xd5d4x10[_0x8289[35]](_0xd5d4x11[1]);_0xd5d4x10[_0x8289[35]](_0xd5d4x11[2]);_0xd5d4x10[_0x8289[35]](_0xd5d4x11[3]);var _0xd5d4x12= new Array(_0xd5d4x7[_0x8289[24]]);for(var _0xd5d4xc=0;_0xd5d4xc<_0xd5d4x7[_0x8289[24]];++_0xd5d4xc){_0xd5d4x12[_0xd5d4xc]=_0xd5d4x7[_0xd5d4xc][_0x8289[25]](0)};_0xd5d4x1a(_0xd5d4x12,_0xd5d4x9,_0xd5d4x10);var _0xd5d4x13=_0xd5d4x2[_0x8289[16]][_0x8289[15]]();var _0xd5d4x14;if(_0xd5d4x5===_0x8289[13]){_0xd5d4x14=_0xd5d4x12[_0x8289[24]]}else {if(_0xd5d4x5===_0x8289[5]){_0xd5d4x14=_0xd5d4x12[_0x8289[24]]-2}else {_0xd5d4x14=_0xd5d4x12[_0x8289[24]]-4}};for(var _0xd5d4xc=0;_0xd5d4xc<_0xd5d4x14;++_0xd5d4xc){_0xd5d4x13[_0x8289[35]](_0xd5d4x12[_0xd5d4xc][0]);if(_0xd5d4xc!==_0xd5d4x14-1||_0xd5d4x5!==_0x8289[10]){_0xd5d4x13[_0x8289[35]](_0xd5d4x12[_0xd5d4xc][1])};};return _0xd5d4x13;};return _0xd5d4xb;};var _0xd5d4x15=null;var _0xd5d4x16=false;var _0xd5d4x17=null;var _0xd5d4x18=null;function _0xd5d4x19(){_0xd5d4x15=String[_0x8289[36]](128);_0xd5d4x15+=_0xd5d4x2[_0x8289[16]][_0x8289[37]](String[_0x8289[36]](0x00),128);_0xd5d4x17=[[0x428a2f98,0xd728ae22],[0x71374491,0x23ef65cd],[0xb5c0fbcf,0xec4d3b2f],[0xe9b5dba5,0x8189dbbc],[0x3956c25b,0xf348b538],[0x59f111f1,0xb605d019],[0x923f82a4,0xaf194f9b],[0xab1c5ed5,0xda6d8118],[0xd807aa98,0xa3030242],[0x12835b01,0x45706fbe],[0x243185be,0x4ee4b28c],[0x550c7dc3,0xd5ffb4e2],[0x72be5d74,0xf27b896f],[0x80deb1fe,0x3b1696b1],[0x9bdc06a7,0x25c71235],[0xc19bf174,0xcf692694],[0xe49b69c1,0x9ef14ad2],[0xefbe4786,0x384f25e3],[0x0fc19dc6,0x8b8cd5b5],[0x240ca1cc,0x77ac9c65],[0x2de92c6f,0x592b0275],[0x4a7484aa,0x6ea6e483],[0x5cb0a9dc,0xbd41fbd4],[0x76f988da,0x831153b5],[0x983e5152,0xee66dfab],[0xa831c66d,0x2db43210],[0xb00327c8,0x98fb213f],[0xbf597fc7,0xbeef0ee4],[0xc6e00bf3,0x3da88fc2],[0xd5a79147,0x930aa725],[0x06ca6351,0xe003826f],[0x14292967,0x0a0e6e70],[0x27b70a85,0x46d22ffc],[0x2e1b2138,0x5c26c926],[0x4d2c6dfc,0x5ac42aed],[0x53380d13,0x9d95b3df],[0x650a7354,0x8baf63de],[0x766a0abb,0x3c77b2a8],[0x81c2c92e,0x47edaee6],[0x92722c85,0x1482353b],[0xa2bfe8a1,0x4cf10364],[0xa81a664b,0xbc423001],[0xc24b8b70,0xd0f89791],[0xc76c51a3,0x0654be30],[0xd192e819,0xd6ef5218],[0xd6990624,0x5565a910],[0xf40e3585,0x5771202a],[0x106aa070,0x32bbd1b8],[0x19a4c116,0xb8d2d0c8],[0x1e376c08,0x5141ab53],[0x2748774c,0xdf8eeb99],[0x34b0bcb5,0xe19b48a8],[0x391c0cb3,0xc5c95a63],[0x4ed8aa4a,0xe3418acb],[0x5b9cca4f,0x7763e373],[0x682e6ff3,0xd6b2b8a3],[0x748f82ee,0x5defb2fc],[0x78a5636f,0x43172f60],[0x84c87814,0xa1f0ab72],[0x8cc70208,0x1a6439ec],[0x90befffa,0x23631e28],[0xa4506ceb,0xde82bde9],[0xbef9a3f7,0xb2c67915],[0xc67178f2,0xe372532b],[0xca273ece,0xea26619c],[0xd186b8c7,0x21c0c207],[0xeada7dd6,0xcde0eb1e],[0xf57d4f7f,0xee6ed178],[0x06f067aa,0x72176fba],[0x0a637dc5,0xa2c898a6],[0x113f9804,0xbef90dae],[0x1b710b35,0x131c471b],[0x28db77f5,0x23047d84],[0x32caab7b,0x40c72493],[0x3c9ebe0a,0x15c9bebc],[0x431d67c4,0x9c100d4c],[0x4cc5d4be,0xcb3e42b6],[0x597f299c,0xfc657e2a],[0x5fcb6fab,0x3ad6faec],[0x6c44198c,0x4a475817]];_0xd5d4x18={};_0xd5d4x18[_0x8289[13]]=[[0x6a09e667,0xf3bcc908],[0xbb67ae85,0x84caa73b],[0x3c6ef372,0xfe94f82b],[0xa54ff53a,0x5f1d36f1],[0x510e527f,0xade682d1],[0x9b05688c,0x2b3e6c1f],[0x1f83d9ab,0xfb41bd6b],[0x5be0cd19,0x137e2179]];_0xd5d4x18[_0x8289[5]]=[[0xcbbb9d5d,0xc1059ed8],[0x629a292a,0x367cd507],[0x9159015a,0x3070dd17],[0x152fecd8,0xf70e5939],[0x67332667,0xffc00b31],[0x8eb44a87,0x68581511],[0xdb0c2e0d,0x64f98fa7],[0x47b5481d,0xbefa4fa4]];_0xd5d4x18[_0x8289[7]]=[[0x22312194,0xFC2BF72C],[0x9F555FA3,0xC84C64C2],[0x2393B86B,0x6F53B151],[0x96387719,0x5940EABD],[0x96283EE2,0xA88EFFE3],[0xBE5E1E25,0x53863992],[0x2B0199FC,0x2C85B8AA],[0x0EB72DDC,0x81C52CA2]];_0xd5d4x18[_0x8289[10]]=[[0x8C3D37C8,0x19544DA2],[0x73E19966,0x89DCD4D6],[0x1DFAB7AE,0x32FF9C82],[0x679DD514,0x582F9FCF],[0x0F6D2B69,0x7BD44DA8],[0x77E36F73,0x04C48942],[0x3F9D85A8,0x6A1D36C8],[0x1112E6AD,0x91D692A1]];_0xd5d4x16=true;}function _0xd5d4x1a(_0xd5d4x1b,_0xd5d4x1c,_0xd5d4x1d){var _0xd5d4x1e,_0xd5d4x1f;var _0xd5d4x20,_0xd5d4x21;var _0xd5d4x22,_0xd5d4x23;var _0xd5d4x24,_0xd5d4x25;var _0xd5d4x26,_0xd5d4x27;var _0xd5d4x28,_0xd5d4x29;var _0xd5d4x2a,_0xd5d4x2b;var _0xd5d4x2c,_0xd5d4x2d;var _0xd5d4x2e,_0xd5d4x2f;var _0xd5d4x30,_0xd5d4x31;var _0xd5d4x32,_0xd5d4x33;var _0xd5d4x34,_0xd5d4x35;var _0xd5d4x36,_0xd5d4x37;var _0xd5d4x38,_0xd5d4x39;var _0xd5d4xc,_0xd5d4x3a,_0xd5d4x3b,_0xd5d4x3c,_0xd5d4x3d,_0xd5d4x3e,_0xd5d4x3f;var _0xd5d4xf=_0xd5d4x1d[_0x8289[24]]();while(_0xd5d4xf>=128){for(_0xd5d4xc=0;_0xd5d4xc<16;++_0xd5d4xc){_0xd5d4x1c[_0xd5d4xc][0]=_0xd5d4x1d[_0x8289[38]]()>>>0;_0xd5d4x1c[_0xd5d4xc][1]=_0xd5d4x1d[_0x8289[38]]()>>>0;};for(;_0xd5d4xc<80;++_0xd5d4xc){_0xd5d4x3c=_0xd5d4x1c[_0xd5d4xc-2];_0xd5d4x3a=_0xd5d4x3c[0];_0xd5d4x3b=_0xd5d4x3c[1];_0xd5d4x1e=(((_0xd5d4x3a>>>19)|(_0xd5d4x3b<<13))^((_0xd5d4x3b>>>29)|(_0xd5d4x3a<<3))^(_0xd5d4x3a>>>6))>>>0;_0xd5d4x1f=(((_0xd5d4x3a<<13)|(_0xd5d4x3b>>>19))^((_0xd5d4x3b<<3)|(_0xd5d4x3a>>>29))^((_0xd5d4x3a<<26)|(_0xd5d4x3b>>>6)))>>>0;_0xd5d4x3e=_0xd5d4x1c[_0xd5d4xc-15];_0xd5d4x3a=_0xd5d4x3e[0];_0xd5d4x3b=_0xd5d4x3e[1];_0xd5d4x20=(((_0xd5d4x3a>>>1)|(_0xd5d4x3b<<31))^((_0xd5d4x3a>>>8)|(_0xd5d4x3b<<24))^(_0xd5d4x3a>>>7))>>>0;_0xd5d4x21=(((_0xd5d4x3a<<31)|(_0xd5d4x3b>>>1))^((_0xd5d4x3a<<24)|(_0xd5d4x3b>>>8))^((_0xd5d4x3a<<25)|(_0xd5d4x3b>>>7)))>>>0;_0xd5d4x3d=_0xd5d4x1c[_0xd5d4xc-7];_0xd5d4x3f=_0xd5d4x1c[_0xd5d4xc-16];_0xd5d4x3b=(_0xd5d4x1f+_0xd5d4x3d[1]+_0xd5d4x21+_0xd5d4x3f[1]);_0xd5d4x1c[_0xd5d4xc][0]=(_0xd5d4x1e+_0xd5d4x3d[0]+_0xd5d4x20+_0xd5d4x3f[0]+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1c[_0xd5d4xc][1]=_0xd5d4x3b>>>0;};_0xd5d4x2a=_0xd5d4x1b[0][0];_0xd5d4x2b=_0xd5d4x1b[0][1];_0xd5d4x2c=_0xd5d4x1b[1][0];_0xd5d4x2d=_0xd5d4x1b[1][1];_0xd5d4x2e=_0xd5d4x1b[2][0];_0xd5d4x2f=_0xd5d4x1b[2][1];_0xd5d4x30=_0xd5d4x1b[3][0];_0xd5d4x31=_0xd5d4x1b[3][1];_0xd5d4x32=_0xd5d4x1b[4][0];_0xd5d4x33=_0xd5d4x1b[4][1];_0xd5d4x34=_0xd5d4x1b[5][0];_0xd5d4x35=_0xd5d4x1b[5][1];_0xd5d4x36=_0xd5d4x1b[6][0];_0xd5d4x37=_0xd5d4x1b[6][1];_0xd5d4x38=_0xd5d4x1b[7][0];_0xd5d4x39=_0xd5d4x1b[7][1];for(_0xd5d4xc=0;_0xd5d4xc<80;++_0xd5d4xc){_0xd5d4x24=(((_0xd5d4x32>>>14)|(_0xd5d4x33<<18))^((_0xd5d4x32>>>18)|(_0xd5d4x33<<14))^((_0xd5d4x33>>>9)|(_0xd5d4x32<<23)))>>>0;_0xd5d4x25=(((_0xd5d4x32<<18)|(_0xd5d4x33>>>14))^((_0xd5d4x32<<14)|(_0xd5d4x33>>>18))^((_0xd5d4x33<<23)|(_0xd5d4x32>>>9)))>>>0;_0xd5d4x26=(_0xd5d4x36^(_0xd5d4x32&(_0xd5d4x34^_0xd5d4x36)))>>>0;_0xd5d4x27=(_0xd5d4x37^(_0xd5d4x33&(_0xd5d4x35^_0xd5d4x37)))>>>0;_0xd5d4x22=(((_0xd5d4x2a>>>28)|(_0xd5d4x2b<<4))^((_0xd5d4x2b>>>2)|(_0xd5d4x2a<<30))^((_0xd5d4x2b>>>7)|(_0xd5d4x2a<<25)))>>>0;_0xd5d4x23=(((_0xd5d4x2a<<4)|(_0xd5d4x2b>>>28))^((_0xd5d4x2b<<30)|(_0xd5d4x2a>>>2))^((_0xd5d4x2b<<25)|(_0xd5d4x2a>>>7)))>>>0;_0xd5d4x28=((_0xd5d4x2a&_0xd5d4x2c)|(_0xd5d4x2e&(_0xd5d4x2a^_0xd5d4x2c)))>>>0;_0xd5d4x29=((_0xd5d4x2b&_0xd5d4x2d)|(_0xd5d4x2f&(_0xd5d4x2b^_0xd5d4x2d)))>>>0;_0xd5d4x3b=(_0xd5d4x39+_0xd5d4x25+_0xd5d4x27+_0xd5d4x17[_0xd5d4xc][1]+_0xd5d4x1c[_0xd5d4xc][1]);_0xd5d4x1e=(_0xd5d4x38+_0xd5d4x24+_0xd5d4x26+_0xd5d4x17[_0xd5d4xc][0]+_0xd5d4x1c[_0xd5d4xc][0]+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1f=_0xd5d4x3b>>>0;_0xd5d4x3b=_0xd5d4x23+_0xd5d4x29;_0xd5d4x20=(_0xd5d4x22+_0xd5d4x28+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x21=_0xd5d4x3b>>>0;_0xd5d4x38=_0xd5d4x36;_0xd5d4x39=_0xd5d4x37;_0xd5d4x36=_0xd5d4x34;_0xd5d4x37=_0xd5d4x35;_0xd5d4x34=_0xd5d4x32;_0xd5d4x35=_0xd5d4x33;_0xd5d4x3b=_0xd5d4x31+_0xd5d4x1f;_0xd5d4x32=(_0xd5d4x30+_0xd5d4x1e+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x33=_0xd5d4x3b>>>0;_0xd5d4x30=_0xd5d4x2e;_0xd5d4x31=_0xd5d4x2f;_0xd5d4x2e=_0xd5d4x2c;_0xd5d4x2f=_0xd5d4x2d;_0xd5d4x2c=_0xd5d4x2a;_0xd5d4x2d=_0xd5d4x2b;_0xd5d4x3b=_0xd5d4x1f+_0xd5d4x21;_0xd5d4x2a=(_0xd5d4x1e+_0xd5d4x20+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x2b=_0xd5d4x3b>>>0;};_0xd5d4x3b=_0xd5d4x1b[0][1]+_0xd5d4x2b;_0xd5d4x1b[0][0]=(_0xd5d4x1b[0][0]+_0xd5d4x2a+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1b[0][1]=_0xd5d4x3b>>>0;_0xd5d4x3b=_0xd5d4x1b[1][1]+_0xd5d4x2d;_0xd5d4x1b[1][0]=(_0xd5d4x1b[1][0]+_0xd5d4x2c+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1b[1][1]=_0xd5d4x3b>>>0;_0xd5d4x3b=_0xd5d4x1b[2][1]+_0xd5d4x2f;_0xd5d4x1b[2][0]=(_0xd5d4x1b[2][0]+_0xd5d4x2e+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1b[2][1]=_0xd5d4x3b>>>0;_0xd5d4x3b=_0xd5d4x1b[3][1]+_0xd5d4x31;_0xd5d4x1b[3][0]=(_0xd5d4x1b[3][0]+_0xd5d4x30+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1b[3][1]=_0xd5d4x3b>>>0;_0xd5d4x3b=_0xd5d4x1b[4][1]+_0xd5d4x33;_0xd5d4x1b[4][0]=(_0xd5d4x1b[4][0]+_0xd5d4x32+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1b[4][1]=_0xd5d4x3b>>>0;_0xd5d4x3b=_0xd5d4x1b[5][1]+_0xd5d4x35;_0xd5d4x1b[5][0]=(_0xd5d4x1b[5][0]+_0xd5d4x34+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1b[5][1]=_0xd5d4x3b>>>0;_0xd5d4x3b=_0xd5d4x1b[6][1]+_0xd5d4x37;_0xd5d4x1b[6][0]=(_0xd5d4x1b[6][0]+_0xd5d4x36+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1b[6][1]=_0xd5d4x3b>>>0;_0xd5d4x3b=_0xd5d4x1b[7][1]+_0xd5d4x39;_0xd5d4x1b[7][0]=(_0xd5d4x1b[7][0]+_0xd5d4x38+((_0xd5d4x3b/0x100000000)>>>0))>>>0;_0xd5d4x1b[7][1]=_0xd5d4x3b>>>0;_0xd5d4xf-=128;};}}var _0xd5d4x40=_0x8289[0];if( typeof define!==_0x8289[39]){if( typeof module===_0x8289[40]&&module[_0x8289[41]]){var _0xd5d4x41=true;define=function(_0xd5d4x42,_0xd5d4x43){_0xd5d4x43(require,module)};}else {if( typeof forge===_0x8289[12]){forge={}};return _0xd5d4x1(forge);}};var _0xd5d4x44;var _0xd5d4x45=function(_0xd5d4x46,_0xd5d4x47){_0xd5d4x47[_0x8289[41]]=function(_0xd5d4x2){var _0xd5d4x48=_0xd5d4x44[_0x8289[43]](function(_0xd5d4x49){return _0xd5d4x46(_0xd5d4x49)})[_0x8289[42]](_0xd5d4x1);_0xd5d4x2=_0xd5d4x2||{};_0xd5d4x2[_0x8289[44]]=_0xd5d4x2[_0x8289[44]]||{};if(_0xd5d4x2[_0x8289[44]][_0xd5d4x40]){return _0xd5d4x2[_0xd5d4x40]};_0xd5d4x2[_0x8289[44]][_0xd5d4x40]=true;for(var _0xd5d4xc=0;_0xd5d4xc<_0xd5d4x48[_0x8289[24]];++_0xd5d4xc){_0xd5d4x48[_0xd5d4xc](_0xd5d4x2)};return _0xd5d4x2[_0xd5d4x40];}};var _0xd5d4x4a=define;define=function(_0xd5d4x42,_0xd5d4x43){_0xd5d4x44=( typeof _0xd5d4x42===_0x8289[45])?_0xd5d4x43[_0x8289[25]](2):_0xd5d4x42[_0x8289[25]](2);if(_0xd5d4x41){delete define;return _0xd5d4x4a[_0x8289[48]](null,Array[_0x8289[47]][_0x8289[25]][_0x8289[46]](arguments,0));};define=_0xd5d4x4a;return define[_0x8289[48]](null,Array[_0x8289[47]][_0x8289[25]][_0x8289[46]](arguments,0));};define([_0x8289[49],_0x8289[50],_0x8289[51]],function(){_0xd5d4x45[_0x8289[48]](null,Array[_0x8289[47]][_0x8289[25]][_0x8289[46]](arguments,0))});})();