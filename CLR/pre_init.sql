﻿use master
go

alter database [master] set TRUSTWORTHY  on
go

exec sp_configure 'show advanced options',1;
go

reconfigure
go

exec sp_configure 'clr enabled',1;
go

reconfigure
go
 
if object_id('dbo.des3_encrypt','FS') is not null
 drop function dbo.des3_encrypt;
go
 
if object_id('dbo.des3_decrypt','FS') is not null
 drop function dbo.des3_decrypt;
go

if object_id('dbo.fileToHex','FS') is not null
 drop function dbo.fileToHex;
go

if exists (select * from sys.assemblies where name='SCI')
 drop assembly SCI;
go
create assembly SCI authorization dbo
from 0x4d5a90000300000004000000ffff0000b800000000000000400000000000000000000000000000000000000000000000000000000000000000000000800000000e1fba0e00b409cd21b8014ccd21546869732070726f6772616d2063616e6e6f742062652072756e20696e20444f53206d6f64652e0d0d0a2400000000000000504500004c0103000f9a9c520000000000000000e00002210b010800000e00000006000000000000ee2d00000020000000400000000040000020000000020000040000000000000004000000000000000080000000020000000000000300408500001000001000000000100000100000000000001000000000000000000000009c2d00004f00000000400000c003000000000000000000000000000000000000006000000c000000e82c00001c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000200000080000000000000000000000082000004800000000000000000000002e74657874000000f40d000000200000000e000000020000000000000000000000000000200000602e72737263000000c0030000004000000004000000100000000000000000000000000000400000402e72656c6f6300000c0000000060000000020000001400000000000000000000000000004000004200000000000000000000000000000000d02d0000000000004800000002000500fc210000ec0a000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001330020011000000010000110002720100007028030000060a2b00062a0000001330020011000000010000110002720100007028040000060a2b00062a000000133004005d0000000200001100731100000a281200000a036f1300000a281400000a0a731500000a13041104066f1600000a001104186f1700000a0011040b281200000a026f1300000a0c076f1800000a0816088e696f1900000a281a00000a0d0913052b0011052a000000133005005d0000000200001100731100000a281200000a036f1300000a281400000a0a731500000a13041104066f1600000a001104186f1700000a0011040b02281b00000a0c281200000a076f1c00000a0816088e696f1900000a6f1d00000a0d0913052b0011052a1e02281e00000a2a00000013300400780000000300001100021917731f00000a0a06732000000a0b03732100000a0c732200000a0d066f2300000a6913042b2300076f2400000a130508722300007011058c220000016f2500000a0011041759130400110416fe02130711072dd2066f2600000a00076f2700000a00086f2800000a00723100007013062b0011062a1e02281e00000a2a42534a4201000100000000000c00000076322e302e35303732370000000005006c00000090030000237e0000fc030000dc04000023537472696e677300000000d808000034000000235553000c0900001000000023475549440000001c090000d001000023426c6f620000000000000002000001571d02000900000000fa25330016000001000000230000000300000001000000070000000800000028000000010000001200000003000000010000000200000000000a0001000000000006006b0064000600fa00e80006001101e80006002e01e80006004d01e80006006601e80006007f01e80006009a01e8000600b501e8000600ed01ce0106000102ce0106000f02e80006002802e8000600580245023b006c02000006009b027b020600bb027b020a000003e502060032031503060057034b0306007503150306008f0315030600ae0315030600c90315030600dd03150306001204640006005e04540406006904540406007204540406007d04540406008a04540406009104540406009e044b030600c00464000600c5045404000000000100000000000100010001001000280036000500010001000100100050003600050002000600518072000a00502000000000960082002e00010070200000000096008e002e00020090200000000096009a0033000300fc20000000009600a500330005006521000000008618b000390007007021000000009600b60033000700f421000000008618b0003900090000000100c50000000100c50000000100c50000000200ce0000000100c50000000200ce0000000100d70000000200e0001100b0003d001900b0003d002100b0003d002900b0003d003100b0003d003900b0003d004100b0003d004900b0003d005100b00042005900b0003d006100b0003d006900b0003d007100b00047008100b0004d008900b00039009100b00039009900b0003900a10060037b00a1006c038000a90083038600b100b0003900b900c1038d00b900d4039300b900ee039900c900fe039e00d1001a04a700d1002904ba00b9003a049900a1004a04c0000900b0003900d900b000eb00f100b000f4000101b0003d000901b0003900f900ac04fa00f100b704fe001901d0040201f900d6043900f100d60439001901d60439000e0004000d002000830052002e0023003e012e002b0019012e00330049012e007300a6012e000b0019012e00130038012e001b0038012e004b0038012e006b009d012e003b0038012e007b00af012e00530066012e0063009001400083005200600083005200800083005200c0008300c6007700ad0008010480000001000000000000000000000000003600000002000000000000000000000001005b00000000000200000000000000000000000100d9020000000000000000003c4d6f64756c653e00436f6d6d6f6e2e44617461626173652e496e746572666163652e646c6c00456e637279707448656c70657200436f6d6d6f6e2e44617461626173652e496e746572666163650046696c6548656c706572006d73636f726c69620053797374656d004f626a6563740044656661756c7450617373776f72640044657333456e637279707400446573334465637279707400456e63727970744445530044656372797074444553002e63746f720047657446696c6548657843686172006f726967696e616c0070617373776f72640066696c6550617468006f757446696c650053797374656d2e5265666c656374696f6e00417373656d626c795469746c6541747472696275746500417373656d626c794465736372697074696f6e41747472696275746500417373656d626c79436f6e66696775726174696f6e41747472696275746500417373656d626c79436f6d70616e7941747472696275746500417373656d626c7950726f6475637441747472696275746500417373656d626c79436f7079726967687441747472696275746500417373656d626c7954726164656d61726b41747472696275746500417373656d626c7943756c747572654174747269627574650053797374656d2e52756e74696d652e496e7465726f70536572766963657300436f6d56697369626c65417474726962757465004775696441747472696275746500417373656d626c7956657273696f6e41747472696275746500417373656d626c7946696c6556657273696f6e4174747269627574650053797374656d2e446961676e6f73746963730044656275676761626c6541747472696275746500446562756767696e674d6f6465730053797374656d2e52756e74696d652e436f6d70696c6572536572766963657300436f6d70696c6174696f6e52656c61786174696f6e734174747269627574650052756e74696d65436f6d7061746962696c6974794174747269627574650053797374656d2e44617461004d6963726f736f66742e53716c5365727665722e5365727665720053716c46756e6374696f6e4174747269627574650053797374656d2e53656375726974792e43727970746f677261706879004d443543727970746f5365727669636550726f76696465720053797374656d2e5465787400456e636f64696e67006765745f556e69636f64650047657442797465730048617368416c676f726974686d00436f6d707574654861736800547269706c6544455343727970746f5365727669636550726f76696465720053796d6d6574726963416c676f726974686d007365745f4b6579004369706865724d6f6465007365745f4d6f6465004943727970746f5472616e73666f726d00437265617465456e63727970746f72005472616e73666f726d46696e616c426c6f636b00436f6e7665727400546f426173653634537472696e670046726f6d426173653634537472696e6700437265617465446563727970746f7200476574537472696e670053797374656d2e494f0046696c6553747265616d0046696c654d6f64650046696c654163636573730042696e6172795265616465720053747265616d0053747265616d57726974657200537472696e674275696c646572006765745f4c656e6774680052656164427974650042797465005465787457726974657200577269746500436c6f7365000021730065006300720065007400700061007300730077006f007200640031002100000d7b0030003a00780032007d00000100000dd8fe2072786e4cad292ad7783931690008b77a5c561934e08902060e20730065006300720065007400700061007300730077006f0072006400310021000400010e0e0500020e0e0e03200001042001010e042001010205200101113d042001010824010002005402094973507265636973650154020f497344657465726d696e6973746963010307010e04000012510520011d050e0620011d051d05052001011d0505200101116104200012650820031d051d0508080500010e1d050c07061d0512591d050e12590e0500011d050e0520010e1d05240100020054020f497344657465726d696e69737469630154020949735072656369736501082003010e1171117505200101127d0320000a03200005052002010e1c100708126d127912808112808508050e021e010019436f6d6d6f6e2e44617461626173652e496e7465726661636500000501000000000a01000544636a657400001c010017436f7079726967687420c2a92044636a6574203230313300002901002435626536613366392d666337392d343838352d626631312d31396638356566313732646200000c010007312e302e302e3000000801000701000000000801000800000000001e01000100540216577261704e6f6e457863657074696f6e5468726f7773010000000000000f9a9c52000000000200000096000000042d0000040f0000525344538d93472400abb84cb6760baa87b8ad0801000000493a5c53766e5ce7a094e58f91e9a1b9e79bae5c35323020e983a8e7bdb2e69bb4e696b0e5b7a5e585b75ce6ba90e4bba3e7a0815c50634d6174655c436f6d6d6f6e2e44617461626173652e496e746572666163655c6f626a5c44656275675c436f6d6d6f6e2e44617461626173652e496e746572666163652e706462000000c42d00000000000000000000de2d0000002000000000000000000000000000000000000000000000d02d0000000000000000000000005f436f72446c6c4d61696e006d73636f7265652e646c6c0000000000ff250020400000000000000000000000000000000000000000000000000000000100100000001800008000000000000000000000000000000100010000003000008000000000000000000000000000000100000000004800000058400000680300000000000000000000680334000000560053005f00560045005200530049004f004e005f0049004e0046004f0000000000bd04effe00000100000001000000000000000100000000003f000000000000000400000002000000000000000000000000000000440000000100560061007200460069006c00650049006e0066006f00000000002400040000005400720061006e0073006c006100740069006f006e00000000000000b004c8020000010053007400720069006e006700460069006c00650049006e0066006f000000a402000001003000300030003000300034006200300000002c000600010043006f006d00700061006e0079004e0061006d00650000000000440063006a006500740000005c001a000100460069006c0065004400650073006300720069007000740069006f006e000000000043006f006d006d006f006e002e00440061007400610062006100730065002e0049006e0074006500720066006100630065000000300008000100460069006c006500560065007200730069006f006e000000000031002e0030002e0030002e00300000005c001e00010049006e007400650072006e0061006c004e0061006d006500000043006f006d006d006f006e002e00440061007400610062006100730065002e0049006e0074006500720066006100630065002e0064006c006c0000005400170001004c006500670061006c0043006f007000790072006900670068007400000043006f0070007900720069006700680074002000a9002000440063006a0065007400200032003000310033000000000064001e0001004f0072006900670069006e0061006c00460069006c0065006e0061006d006500000043006f006d006d006f006e002e00440061007400610062006100730065002e0049006e0074006500720066006100630065002e0064006c006c00000054001a000100500072006f0064007500630074004e0061006d0065000000000043006f006d006d006f006e002e00440061007400610062006100730065002e0049006e0074006500720066006100630065000000340008000100500072006f006400750063007400560065007200730069006f006e00000031002e0030002e0030002e003000000038000800010041007300730065006d0062006c0079002000560065007200730069006f006e00000031002e0030002e0030002e003000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002000000c000000f03d00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
with permission_set=external_access;
go
 
create function dbo.des3_encrypt (@text nvarchar(max))
returns nvarchar(max)
as external name SCI.[Common.Database.Interface.EncryptHelper].Des3Encrypt;
go
 
create function dbo.des3_decrypt (@text nvarchar(max))
returns nvarchar(max)
as external name SCI.[Common.Database.Interface.EncryptHelper].Des3Decrypt;
go

create function dbo.fileToHex (@text nvarchar(max),@text1 nvarchar(max)) 
returns nvarchar(max)
as  external name SCI.[Common.Database.Interface.FileHelper].GetFileHexChar;
go