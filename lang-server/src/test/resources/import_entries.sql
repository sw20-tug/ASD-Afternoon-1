-- required as not to violate consistency check
insert into VOCABULARIES(name, id , sourcelanguage, targetlanguage) values ('dummy', 1, 'en', 'de')

insert into ENTRIES (phrase, translation, id, fk_vocabulary) values ('foo', 'bar', 1, 1)
insert into ENTRIES (phrase, translation, id, fk_vocabulary) values ('foo', 'baz', 2, 1)
insert into ENTRIES (phrase, translation, id, fk_vocabulary) values ('bar', 'baz', 3, 1)
