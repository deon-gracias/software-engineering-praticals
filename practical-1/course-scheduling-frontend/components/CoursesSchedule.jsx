import { Box, Button, Paper, Table, Tabs, Title } from "@mantine/core";

function splitTimes(time) {
  let res = time.split(/(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)/);

  //   console.log(res);
  //   console.log(times);
  //   return Object.values(times);
  return res;
}

export default function CoursesSchedule({
  resetSchedule,
  schedule,
  removeBack,
}) {

  const views = [
    { name: "Standard", component: <StandardView schedule={schedule} /> },
    { name: "Rooms", component: <RoomView schedule={schedule} /> },
    { name: "Courses", component: <CourseView schedule={schedule} /> },
  ];

  return (
    <>
      {schedule !== "" && (
        <Paper
          shadow="xs"
          sx={{ padding: "2rem", width: "100%", height: "100%" }}
        >
          {!removeBack && <Button onClick={resetSchedule}>Back</Button>}
          <Title order={4} mt={25} mb={10}>
            Schedule
          </Title>

          <Tabs defaultValue={"Standard"}>
            <Tabs.List>
              {views.map((e) => (
                <Tabs.Tab value={e.name}>{e.name}</Tabs.Tab>
              ))}
            </Tabs.List>

            {typeof schedule === "object" ? (
              views.map((e) => (
                <Tabs.Panel value={e.name}>{e.component}</Tabs.Panel>
              ))
            ) : (
              <Tabs.Panel value="Standard">{schedule}</Tabs.Panel>
            )}
          </Tabs>
        </Paper>
      )}
    </>
  );
}

function StandardView({ schedule }) {
  return (
    <Table>
      <thead>
        <tr>
          <th>Timings</th>
          <th>Courses</th>
          <th>Room</th>
        </tr>
      </thead>
      <tbody>
        {Object.keys(schedule.times).map((e) => (
          <tr key={e}>
            <td>{e}</td>
            <td>{schedule.times[e]}</td>
            <td>{schedule.rooms[schedule.times[e]]}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}

function RoomView({ schedule }) {
  return (
    Object.entries(schedule.roomWise).map(([room, course]) => (
      <Box sx={{ padding: "1rem" }}>
        <Title ml={"0.5rem"} order={4}>
          Room {room}
        </Title>
        <Table>
          <thead>
            <tr>
              <th>Course</th>
              <th>Day</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            {Object.entries(course).map(([key, value]) => (
              <tr>
                <td>{key}</td>
                <td>{letterToDay(value[0])}</td>
                <td>
                  {value[1]} {parseInt(value[1].split(":")) > 9 ? "am" : "pm"}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Box>
    ))
  );
}

function CourseView({ schedule }) {
  return (
    schedule.courseWise !== undefined && Object.entries(schedule.courseWise).map(([course, room]) => (
      <Box sx={{ padding: "1rem" }}>
        <Title ml={"0.5rem"} order={4}>
          Course {course}
        </Title>
        <Table>
          <thead>
            <tr>
              <th>Room</th>
              <th>Day</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            {Object.entries(room).map(([key, value]) => (
              <tr>
                <td>{key}</td>
                <td>{letterToDay(value[0])}</td>
                <td>
                  {value[1]} {parseInt(value[1].split(":")) > 9 ? "am" : "pm"}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Box>
    ))
  );
}

function letterToDay(letter) {
  switch (letter) {
    case "M":
      return "Monday";
    case "T":
      return "Tuesday";
    case "W":
      return "Wednesday";
    case "Th":
      return "Thursday";
    case "F":
      return "Friday";

    default:
      return letter;
  }
}
