create table if not exists barcodes_cons
(
    id_registro  integer not null
    primary key,
    id_library   smallint,
    id_barcode_1 varchar(255),
    id_barcode_2 varchar(255)
    );

alter table barcodes_cons
    owner to sisbi;
