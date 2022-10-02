import { Box, Paper, ScrollArea, SimpleGrid } from "@mantine/core";
import Head from "next/head";
import { useState } from "react";
import CoursesForm from "../components/CoursesForm";
import CoursesSchedule from "../components/CoursesSchedule";

export default function Home() {
  const [schedule, setSchedule] = useState("");
  return (
    <>
      <Head>
        <title>Course Scheduling System</title>
      </Head>

      <Box sx={{ padding: "2rem" }}>
        <SimpleGrid
          sx={{ width: "100%", height: "100%" }}
          spacing="lg"
          // cols={2}
          // breakpoints={[{ maxWidth: "md", cols: 1, rows: 2 }]}
        >
          {/* <CoursesForm setSchedule={setSchedule} /> */}

          {/* {Object.keys(schedule).length < 1 ? (
            <CoursesForm setSchedule={setSchedule} />
          ) : (
            <CoursesSchedule
              resetSchedule={() => setSchedule({})}
              schedule={schedule}
            />
          )} */}
          <CoursesForm setSchedule={setSchedule} />
          <CoursesSchedule removeBack
            resetSchedule={() => setSchedule({})}
            schedule={schedule}
          />
        </SimpleGrid>
      </Box>
    </>
  );
}
