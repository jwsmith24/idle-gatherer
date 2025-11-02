export async function GET() {
  const response = await fetch("http://localhost:8080/api/player", {
    cache: "no-cache",
  });
  const data = await response.json();

  return Response.json(data, { status: response.status });
}

export async function POST(request: Request) {
  const body = await request.json();

  const response = await fetch("http://localhost:8080/api/player", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  const data = await response.json();
  return Response.json(data, { status: response.status });
}
