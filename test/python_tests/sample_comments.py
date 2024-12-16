import dotenv, psycopg2, os
dotenv.load_dotenv()

# Connect to your postgres DB
conn = psycopg2.connect(f"dbname=sisbi_db user=sisbi password={os.environ['DB_PASSWORD']}")

# Open a cursor to perform database operations
cur = conn.cursor()

# Execute a query
cur.execute("SELECT id_sample FROM sample")

# Retrieve query results
#los join key son comments.id_type y sample.id_sample
matches = []
records = cur.fetchall()
projs = set()
for idsamples in records:
    cur.execute(f"SELECT id_type from comments where id_type='%s' AND user_name!='SISBI' and type='Sample';", idsamples)
    recordsamples = cur.fetchall()
    if len(recordsamples) > 0:
        #cur.execute("SELECT * FROM sample WHERE id_sample=%s;", idsamples)
        cur.execute("SELECT id_project FROM sample WHERE id_sample=%s;", idsamples)

        samples = cur.fetchall()
        #cols = cur.description
        for sample in samples:
            #assoc = {col.name: sample[icol] for (icol, col) in enumerate(cols)}
            #projs.add(assoc['id_project'])
            if sample[0] not in projs:
                print("sample", sample[0])
            projs.add(sample[0])
    #    matches.extend(recordsamples)
print("projs", projs)