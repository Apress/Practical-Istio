ARG CMD

# Base image
FROM python:3.9-slim

ARG CMD

# Convert ARG to ENV so it's available at runtime
ENV SCRIPT_NAME=${CMD}

# Set working directory
WORKDIR /app

# Copy application code
COPY . .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Default command
CMD python ${SCRIPT_NAME}
